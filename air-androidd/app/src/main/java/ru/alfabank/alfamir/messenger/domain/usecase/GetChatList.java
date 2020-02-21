package ru.alfabank.alfamir.messenger.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.alfabank.alfamir.base_elements.UseCase;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerDataSource;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepository;
import ru.alfabank.alfamir.messenger.domain.mapper.ChatLightMapper;
import ru.alfabank.alfamir.messenger.presentation.dto.ChatLight;

public class GetChatList extends UseCase<GetChatList.RequestValues, GetChatList.ResponseValue> {
    private MessengerRepository mMessengerRepository;
    private ChatLightMapper mChatLightMapper;

    @Inject
    public GetChatList(MessengerRepository messengerRepository,
                       ChatLightMapper chatLightMapper){
        mMessengerRepository = messengerRepository;
        mChatLightMapper = chatLightMapper;
    }

    @Override
    public Observable<ResponseValue> execute(RequestValues requestValues) {
        boolean isCacheDirty = requestValues.getIsCacheDirty();
        return mMessengerRepository.loadChats(new MessengerDataSource
                .LoadChatListRequestValues(isCacheDirty))
                .flatMapIterable(chatLightRaws -> chatLightRaws)
                .map(mChatLightMapper)
                .toList()
                .flatMapObservable(chatLightList ->
                        Observable.just(new ResponseValue(chatLightList)));
    }

    public static class RequestValues implements UseCase.RequestValues {
        private boolean mIsCacheDirty;
        public RequestValues(boolean isCacheDirty){
            mIsCacheDirty = isCacheDirty;
        }
        public boolean getIsCacheDirty() {
            return mIsCacheDirty;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private List<ChatLight> mChatLightList;
        ResponseValue (List<ChatLight> chatLightList){
            mChatLightList = chatLightList;
        }
        public List<ChatLight> getChatLightList() {
            return mChatLightList;
        }
    }
}