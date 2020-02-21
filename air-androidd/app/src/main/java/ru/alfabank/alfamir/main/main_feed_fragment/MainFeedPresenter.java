package ru.alfabank.alfamir.main.main_feed_fragment;

import android.annotation.SuppressLint;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.feed.data.source.repository.PostRepository;
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository;
import ru.alfabank.alfamir.main.main_feed_fragment.contract.MainFeedContract;
import ru.alfabank.alfamir.main.main_feed_fragment.view_dummy.MainFeedViewDummy;
import ru.alfabank.alfamir.survey.data.dto.SurveyCover;
import ru.alfabank.alfamir.survey.data.source.repository.SurveyDataSource;
import ru.alfabank.alfamir.survey.data.source.repository.SurveyRepository;
import ru.alfabank.alfamir.utility.logging.remote.LoggerContract;
import ru.alfabank.alfamir.utility.update_notifier.UpdateNotifier;
import ru.alfabank.alfamir.utility.update_notifier.notifications.SurveyUpdate;

import static ru.alfabank.alfamir.Constants.Feed_new.MAIN_FEED;

public class MainFeedPresenter implements MainFeedContract.Presenter, LoggerContract.Client.NewsFeed {

    private MainFeedContract.View mNewsFragmentView;

    private ImageRepository mImageRepository;
    private PostRepository mPostRepository;
    private SurveyRepository mQuizRepository;

    private boolean mIsLoading;
    private boolean mIsQuizRequestCompleted;
    private boolean mIsNewsRequestCompleted;
    private int mQuizOffset = 0;

    private int mErrorCounter;
    private long mLastTimeErrorShown;
    private long mRetryPace = 2500;
    private long mResetPace = 9000;
    private boolean isToastClickedByUser;

    private String feedName = "Лента";

    private LoggerContract.Provider mLogger;
    private UpdateNotifier mUpdateNotifier;

    private boolean mFirstLoaded = true;
    private CompositeDisposable mCompositeDisposable;

    @SuppressLint("CheckResult")
    @Inject
    MainFeedPresenter(ImageRepository imageRepository,
                      SurveyRepository quizRepository,
                      PostRepository postRepository,
                      UpdateNotifier updateNotifier,
                      LoggerContract.Provider logger) {
        mImageRepository = imageRepository;
        mPostRepository = postRepository;
        mQuizRepository = quizRepository;
        mUpdateNotifier = updateNotifier;
        mLogger = logger;
        mCompositeDisposable = new CompositeDisposable();
        mUpdateNotifier.getConnectionStatusListener().subscribe(notification -> {
            if (notification instanceof SurveyUpdate) refreshSurveyStatus();
        });

    }

    @Override
    public void takeView(MainFeedContract.View view) {
        mNewsFragmentView = view;
        if (mFirstLoaded) {
            loadFeed();
        }
    }

    @Override
    public void dropView() {
        mCompositeDisposable.clear();
        mNewsFragmentView = null;
    }

    @Override
    public MainFeedContract.View getView() {
        if (mNewsFragmentView == null) {
            return new MainFeedViewDummy();
        }
        return mNewsFragmentView;
    }

    @Override
    public void onListRefresh() {
        refreshNews();
    }

    @Override
    public void onLoadMore() {
        if (checkIfBusy()) return;
        changeLoadingStatus(true);
        loadMore();
    }

    @Override
    public void refreshSurveyStatus() {
        mQuizRepository.getNewsInjectionSurvey(new SurveyDataSource.LoadNewsInjectionSurveyCallback() {
            @Override
            public void onNewsInjectionSurveyLoaded(SurveyCover surveyCover) {
                getView().updateSurvey();
                clearLoadingFlags();
                changeLoadingStatus(false);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void onErrorSnackBarActionClicked(int actionType) {
        if (!isToastClickedByUser) {
            isToastClickedByUser = true;
            mErrorCounter = 1;
        }
        mRetryPace = 0;
        switch (actionType) {
            case Constants.TOAST_ACTION_TYPE_LOAD:
                loadFeed();
                break;
            case Constants.TOAST_ACTION_TYPE_LOAD_MORE:
                loadMore();
                break;
        }
    }

    @Override
    public void onErrorSnackBarDismissedItself() {
        isToastClickedByUser = false;
        mRetryPace = 3000;
    }

    @Override
    public void onSearchClicked() {
        getView().openSearchUi();
    }

    private void loadFeed() {
        changeLoadingStatus(true);
        loadNews();
        loadQuiz();
    }

    private void loadQuiz() {
        mQuizRepository.getNewsInjectionSurvey(new SurveyDataSource.LoadNewsInjectionSurveyCallback() {
            @Override
            public void onNewsInjectionSurveyLoaded(SurveyCover surveyCover) {
                if (surveyCover != null) {
                    mQuizOffset = 1;
                }
                mIsQuizRequestCompleted = true;
                onFeedDataLoaded();
            }

            @Override
            public void onDataNotAvailable() {
                mIsQuizRequestCompleted = true;
                mQuizOffset = 0;
                onFeedDataLoaded();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void loadNews() {
        getView().showProgressBar();
        String feedType = MAIN_FEED;
        String timeStamp = "";
        int newsCount = Constants.FEED_UPLOAD_AMOUNT;

        mCompositeDisposable.add(mPostRepository.getMainFeed(feedType, timeStamp, newsCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    mIsNewsRequestCompleted = true;
                    onFeedDataLoaded();
                    mFirstLoaded = false;
                }, throwable -> {
                    mIsNewsRequestCompleted = true;
                    showConnectionError(Constants.TOAST_ACTION_TYPE_LOAD);
                    onFeedDataLoaded();
                }));
    }

    private void loadMore() {
        String feedType = MAIN_FEED;
        String timeStamp = mPostRepository.getLastPostPubTimeMinusOneSecond(feedType);
        getView().showProgressBar();

        mCompositeDisposable.add(mPostRepository.getMainFeed(feedType, timeStamp, Constants.FEED_UPLOAD_AMOUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    int lastNewPosition = mPostRepository.getCachedPostsListSize(feedType) - 1;
                    int firstNewPosition = lastNewPosition - (posts.size() - 1);

                    lastNewPosition = lastNewPosition + mQuizOffset;
                    firstNewPosition = firstNewPosition + mQuizOffset;

                    getView().showMoreNews(firstNewPosition, lastNewPosition);
                    changeLoadingStatus(false);
                    logLoadMore(Constants.FEED_UPLOAD_AMOUNT, firstNewPosition);
                    getView().hideProgressBar();
                }, throwable -> {
                    showConnectionError(Constants.TOAST_ACTION_TYPE_LOAD_MORE);
                    changeLoadingStatus(false);
                    getView().hideProgressBar();
                }));
    }

    private void refreshNews() {
        if (checkIfBusy()) {
            getView().hideProgressBar();
            return;
        }
        String feedId = MAIN_FEED;

        mPostRepository.dropCache(feedId);
        mImageRepository.dropCache();
        getView().clearFeed();

        loadFeed();
        logFeedRefresh(feedId, feedName);
    }

    private void onFeedDataLoaded() {
        if (mIsNewsRequestCompleted && mIsQuizRequestCompleted) {
            getView().hideProgressBar();
            getView().showFeed();
            clearLoadingFlags();
            changeLoadingStatus(false);
        }
    }

    private void clearLoadingFlags() {
        mIsNewsRequestCompleted = false;
        mIsQuizRequestCompleted = false;
    }

    private void showConnectionError(int actionType) {
        long currentTime = new Date().getTime();
        long difference = currentTime - mLastTimeErrorShown;
        if (difference > mResetPace) {
            mRetryPace = 3000;
            mErrorCounter = 0;
            isToastClickedByUser = false;
        }

        if (difference < mRetryPace) return;
        if (mErrorCounter > 2) return;

        mErrorCounter++;
        mLastTimeErrorShown = currentTime;
        getView().showConnectionErrorSnackBar("Что-то пошло не так", "Повторить", actionType);
    }

    private boolean checkIfBusy() {
        return mIsLoading;
    }

    private void changeLoadingStatus(boolean status) {
        mIsLoading = status;
    }

    @Override
    public void logFeedRefresh(String feedId, String feedName) {
        mLogger.refresh(feedId, feedName);
    }

    @Override
    public void logLoadMore(int amount, int current) {
        mLogger.loadMore(amount, current);
    }

}
