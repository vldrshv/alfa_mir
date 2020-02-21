package ru.alfabank.alfamir.messenger.data.source.repository;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.data.source.broadcast_receiver.сonnectionTracker.ConnectionTracker;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.messenger.data.dto.Attachment;
import ru.alfabank.alfamir.messenger.data.dto.ChatLightRaw;
import ru.alfabank.alfamir.messenger.data.dto.ChatRaw;
import ru.alfabank.alfamir.messenger.data.dto.MessageRaw;
import ru.alfabank.alfamir.messenger.data.dto.PollDataRaw;
import ru.alfabank.alfamir.messenger.data.dto.StatusRaw;
import ru.alfabank.alfamir.messenger.data.dto.UserRaw;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

import static java.util.concurrent.TimeUnit.SECONDS;
import static ru.alfabank.alfamir.Constants.Log.LOG_CHAT;

@Singleton
public class MessengerRepository implements MessengerDataSource, PollDataDataSource {
    // subjects that push new getData to subscribers
    private PublishSubject<MessageRaw> mMessageSubject = PublishSubject.create();
    private PublishSubject<UserRaw> mUserSubject = PublishSubject.create();
    private PublishSubject<StatusRaw> mStatusSubject = PublishSubject.create();

    // id of items that server send in poll call
    private List<String> mPullDataIdList = new ArrayList<>();

    private final MessengerDataSource mMessengerRemoteDataSource;

    private List<ChatLightRaw> mChatListCache;
    private Map<String, ChatRaw> mCachedChats = new HashMap<>();
    private Map<String, Boolean> mCachesAreDirty = new HashMap<>();
    private boolean mCacheIsDirty;
    private ConnectionTracker mConnectionHandler;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private boolean mIsOnline;
    private LogWrapper mLog;
    private Object mLock = new Object();
    private boolean mIsChatListCacheDirty;

    @SuppressLint("CheckResult")
    @Inject
    MessengerRepository(@Remote MessengerDataSource messengerRemoteDataSource,
                        ConnectionTracker connectionHandler,
                        LogWrapper log) {
        mMessengerRemoteDataSource = messengerRemoteDataSource;
        mConnectionHandler = connectionHandler;
        mLog = log;
        mConnectionHandler.getConnectionStatusListener().subscribe(isOnline -> {
            mLog.debug(LOG_CHAT, "isOnline = " + isOnline);
            mIsOnline = isOnline;
            if (isOnline) reportOnlineStatus(0);
            if (!isOnline) mCompositeDisposable.clear();
        });
    }

    @SuppressLint("CheckResult")
    public void connect() {
        mConnectionHandler.getConnectionStatusListener().subscribe(isOnline -> {
            mLog.debug(LOG_CHAT, "isOnline = " + isOnline);
            mIsOnline = isOnline;
            if (isOnline) reportOnlineStatus(0);
            if (!isOnline) mCompositeDisposable.clear();
        });
    }

    @SuppressLint("CheckResult")
    private void reportOnlineStatus(int secondsDelay) {
        mCompositeDisposable.add(Observable.timer(secondsDelay, SECONDS)
                .flatMap(lSecondsDelay -> reportOnline())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> pollNewData(new ArrayList<>(), 0),
                        throwable -> reportOnlineStatus(10)));
    }

    @Override
    public Observable<MessageRaw> sendMessage(String chatId, String text, String type) {
        return mMessengerRemoteDataSource.sendMessage(chatId, text, type);
    }

    public Observable<MessageRaw> sendMessage(String chatId, String message, List<Attachment> attachments, String type) {
        return mMessengerRemoteDataSource.sendMessage(chatId, message, attachments, type);
    }

    @Override
    public Observable<String> sendMessageReadStatus(List<Long> ids, String chatId) {
        return mMessengerRemoteDataSource.sendMessageReadStatus(ids, chatId);
    }

    @Override
    public Observable<List<ChatLightRaw>> loadChats(LoadChatListRequestValues requestValues) {
        boolean isCacheDirty = requestValues.getIsCacheDirty();
        if (isCacheDirty || mIsChatListCacheDirty || mChatListCache == null) {
            return mMessengerRemoteDataSource.loadChats(requestValues)
                    .flatMap(chatLightRawList -> {
                        saveChatListToCache(chatLightRawList);
                        return Observable.just(chatLightRawList);
                    });
        }
        return Observable.just(mChatListCache);
    }

    @Override
    public Observable<List<PollDataRaw>> longPoll(List<String> pollDataId) {
        return mMessengerRemoteDataSource
                .longPoll(pollDataId)
                .flatMap(Observable::just);
    }

    @Override
    public Observable<ChatRaw> loadChat(List<String> userId, String chatId, int amount, String type) {
        return mMessengerRemoteDataSource
                .loadChat(userId, chatId, amount, type)
                .doOnNext(chatRaw -> {
                    String chaId = chatRaw.getId();
                    saveToCache(chaId, chatRaw);
                });
    }

    @Override
    public Observable<List<MessageRaw>> loadMoreMessages(String type, String chatId, long messageId, int amount, int direction) {
        return mMessengerRemoteDataSource.loadMoreMessages(type, chatId, messageId, amount, direction);
    }

    @Override
    public Observable<String> reportOnline() {
        return mMessengerRemoteDataSource
                .reportOnline();
    }

    @Override
    public Observable<String> reportOffline() {
        return mMessengerRemoteDataSource
                .reportOffline();
    }

    private void saveToCache(String chatId, ChatRaw chat) {
        mCachedChats.put(chatId, chat);
        mCacheIsDirty = false;
    }

    private void saveChatListToCache(List<ChatLightRaw> chatListCache) {
        if (mChatListCache == null) {
            mChatListCache = new ArrayList<>();
        }
        mChatListCache = chatListCache;
        mIsChatListCacheDirty = false;
    }

    @Override
    public PublishSubject<MessageRaw> subscribeForMessage() {
        return mMessageSubject;
    }

    @Override
    public PublishSubject<UserRaw> subscribeForUser() {
        return mUserSubject;
    }

    @Override
    public PublishSubject<StatusRaw> subscribeForStatus() {
        return mStatusSubject;
    }

    @SuppressLint("CheckResult")
    private void pollNewData(List<String> lPollDataId, int secondsDelay) {
        if (!Constants.Initialization.getMESSENGER_LP_ENABLED())
            return;

        Observable
                .timer(secondsDelay, SECONDS)
                .flatMap(lSecondsDelay -> mMessengerRemoteDataSource.longPoll(lPollDataId))
                .flatMap(pollDataRaws -> {
                    mPullDataIdList.clear();
                    for (PollDataRaw pollDataRaw : pollDataRaws) {
                        String pullDataId = pollDataRaw.getId();
                        mPullDataIdList.add(pullDataId);
                        switch (pollDataRaw.getType()) {
                            case "message":
                                MessageRaw messageRaw = (MessageRaw) pollDataRaw.getValue();
                                mMessageSubject.onNext(messageRaw);
                                break;
                            case "user":
                                UserRaw userRaw = (UserRaw) pollDataRaw.getValue();
                                mUserSubject.onNext(userRaw);
                                break;
                            case "messagestatus":
                                StatusRaw statusRaw = (StatusRaw) pollDataRaw.getValue();
                                mStatusSubject.onNext(statusRaw);
                                break;
                        }
                    }
                    return Observable.just(mPullDataIdList);
                })
                .subscribeOn(Schedulers.io())
                .subscribe(pollDataId -> {
                    if (mIsOnline) pollNewData(pollDataId, 0);
                }, throwable -> {
                    if (mIsOnline) pollNewData(new ArrayList<>(), 10);
                })
        ;
    }

    public void refreshChatList() {
        mIsChatListCacheDirty = true;
    }
}
