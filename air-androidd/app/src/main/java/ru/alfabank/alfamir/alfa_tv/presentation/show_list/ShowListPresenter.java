package ru.alfabank.alfamir.alfa_tv.presentation.show_list;

import android.annotation.SuppressLint;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.alfa_tv.domain.usecase.GetPossibleCurrentShow;
import ru.alfabank.alfamir.alfa_tv.domain.usecase.GetShowCurrentState;
import ru.alfabank.alfamir.alfa_tv.domain.usecase.GetShowList;
import ru.alfabank.alfamir.alfa_tv.domain.usecase.ObserveSavedPassword;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.Show;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.ShowListElement;
import ru.alfabank.alfamir.alfa_tv.presentation.dto.ShowSeparator;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract.ShowListAdapterContract;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.contract.ShowListContract;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.dummy_view.ShowAdapterDummy;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.dummy_view.ShowListDummy;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

import static java.util.concurrent.TimeUnit.SECONDS;


public class ShowListPresenter implements ShowListContract.Presenter {

    private ShowListContract.View mView;
    private ShowListAdapterContract.Adapter mAdapter;

    private List<ShowListElement> mShowListElements;
    private GetShowList mGetShowList;
    private ObserveSavedPassword mObserveSavedPassword;
    private GetShowCurrentState mGetShowCurrentState;
    private GetPossibleCurrentShow mGetPossibleCurrentShow;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private boolean isDataLoaded;
    private boolean isTouchEnabled;
    private LogWrapper mLog;

    @Inject
    ShowListPresenter(GetShowList getShowList,
                      ObserveSavedPassword observeSavedPassword,
                      GetShowCurrentState getShowCurrentState,
                      GetPossibleCurrentShow getPossibleCurrentShow,
                      LogWrapper logWrapper) {
        mGetShowList = getShowList;
        mObserveSavedPassword = observeSavedPassword;
        mGetShowCurrentState = getShowCurrentState;
        mGetPossibleCurrentShow = getPossibleCurrentShow;
        mLog = logWrapper;
    }

    @Override
    public void takeView(ShowListContract.View view) {
        mView = view;
        if (Constants.Initialization.getALFA_TV_ENABLED()) {
            if (!isDataLoaded) {
                getView().enableRefresh();
                getView().showProgressBar();
                boolean isCacheDirty = false;
                loadShowList(isCacheDirty);
            } else {
                isTouchEnabled = true;
                checkPossibleVideoStatus();
            }
        }
        else
            getView().disableRefresh();
    }

    @Override
    public void dropView() {
        mView = null;
        mAdapter = null;
        mCompositeDisposable.dispose();
    }

    @Override
    public ShowListContract.View getView() {
        if (mView == null) return new ShowListDummy();
        return mView;
    }

    @SuppressLint("CheckResult")
    private void loadShowList(boolean isCacheDirty) {
        mCompositeDisposable.add(mGetShowList.execute(new GetShowList.RequestValues(isCacheDirty))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseValue -> {
                    isDataLoaded = true;
                    mShowListElements = responseValue.getShowListElements();
                    getView().showShows();
                    getView().hideProgressBar();
                    isTouchEnabled = true;
                    observeSavedPassword();
                }, throwable -> {
                    getView().hideProgressBar();
                    isTouchEnabled = true;
                    getView().showSnackBar("Что-то пошло не так. Попробуйте позднее");
                }));
    }

    private void observeSavedPassword() {
        mCompositeDisposable.add(mObserveSavedPassword.execute(new ObserveSavedPassword.RequestValues())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseValue -> mCompositeDisposable.add(responseValue.getPSubject()
                        .flatMap(savedPassword -> {
                            int updatedItem = 0;
                            for (int i = 0; i < mShowListElements.size(); i++) {
                                ShowListElement showListElement = mShowListElements.get(i);
                                if (showListElement instanceof Show) {
                                    Show show = (Show) showListElement;
                                    int id = show.getId();
                                    if (id == savedPassword.getId()) {
                                        show.setIsPasswordEntered(true);
                                        updatedItem = i;
                                        break;
                                    }
                                }
                            }
                            return Observable.just(updatedItem);
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(position -> {
                            getAdapter().onItemUpdated(position);
                        }, throwable -> {

                        }))
                )
        );
    }

    @Override
    public void onListRefresh() {
        if(Constants.Initialization.getALFA_TV_ENABLED()) {
            isTouchEnabled = false;
            boolean isCacheDirty = true;
            loadShowList(isCacheDirty);
        }
    }

    /**
     * {@link ShowListAdapterContract.Presenter}  methods
     */

    @Override
    public int getListSize() {
        return mShowListElements.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mShowListElements.get(position).getViewType();
    }

    @Override
    public void takeListAdapter(ShowListAdapterContract.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public ShowListAdapterContract.Adapter getAdapter() {
        if (mAdapter == null) return new ShowAdapterDummy();
        return mAdapter;
    }

    @Override
    public void onShowClicked(int position) {
        if (!isTouchEnabled) return;
        ShowListElement showListElement = mShowListElements.get(position);
        Show show = (Show) showListElement;
        int id = show.getId();
        getView().openShowUi(position, id);
        isTouchEnabled = false;
    }

    @Override
    public void onTimeTicked(int position) {
        if (position == -1) return;
        if (mShowListElements == null) return;
        Show show = (Show) mShowListElements.get(position);
        if (show == null) return;
        long lStartTime = show.getLongStartDate();
        long lEndTime = show.getLongEndDate();
        int progress = getCurrentProgress(lEndTime, lStartTime);
        getAdapter().setProgressBarProgress(position, progress);
    }

    @Override
    public void onTimerEnded(int position) {
        if (position == -1) return;
        if (mShowListElements == null) return;
        Show show = (Show) mShowListElements.get(position);
        if (show == null) return;
        getAdapter().setProgressBarColor(position, Constants.Show.PROGRESS_STATUS_PROLONGED);
        int id = show.getId();
        checkVideoStatus(position, id, 10);
    }

    @Override
    public void bindListRowShow(int position, ShowListAdapterContract.ShowView rowView) {
        Show show = (Show) mShowListElements.get(position);
        if (show == null) return;
        String title = show.getTitle();
        String startDate = show.getStartDate();
        String endDate = show.getEndDate();
        String date = startDate + " - " + endDate;

        int isPasswordRequired = show.getIsPasswordRequired();
        boolean isPasswordEntered = show.getIsPasswordEntered();

        rowView.setTitle(title);
        rowView.setTime(date);
        if (isPasswordRequired == 0) {
            rowView.hidePasswordRequired();
        } else {
            rowView.showPasswordRequired(isPasswordEntered);
        }
    }

    @Override
    public void bindListRowShowCurrent(int position, ShowListAdapterContract.ShowCurrentView rowView) {
        Show show = (Show) mShowListElements.get(position);
        if (show == null) return;
        String title = show.getTitle();
        String startTime = show.getStartDate();
        String endTime = show.getEndDate();
        long lStartTime = show.getLongStartDate();
        long lEndTime = show.getLongEndDate();
        int isPasswordRequired = show.getIsPasswordRequired();
        int isOnAir = show.getIsOnAir();
        boolean isPasswordEntered = show.getIsPasswordEntered();
        int progress = getCurrentProgress(lEndTime, lStartTime);

        rowView.setTitle(title);
        rowView.setStartTime(startTime);
        rowView.setEndTime(endTime);

        switch (isOnAir) {
            case 0: {
                rowView.setProgress(10000);
                rowView.setProgressBarColor(Constants.Show.PROGRESS_STATUS_ENDED);
                break;
            }
            case 1: {
                rowView.setProgress(progress);
                if (progress < 10000) {
                    rowView.setProgressBarColor(Constants.Show.PROGRESS_STATUS_CURRENT);
                    long currentLong = new Date().getTime();
                    long millisUntilFinished = lEndTime - currentLong;
                    long oneTick = (lEndTime - lStartTime) / 10000;
                    rowView.setProgressTimer(millisUntilFinished, oneTick);
                } else {
                    rowView.setProgressBarColor(Constants.Show.PROGRESS_STATUS_PROLONGED);
                }
                break;
            }
        }

        if (isPasswordRequired == 0) {
            rowView.hidePasswordRequired();
        } else {
            rowView.showPasswordRequired(isPasswordEntered);
        }
    }

    @Override
    public void bindListRowShowSeparator(int position, ShowListAdapterContract.ShowSeparatorView rowView) {
        ShowSeparator showSeparator = (ShowSeparator) mShowListElements.get(position);
        if (showSeparator == null) return;
        String title = showSeparator.getDate();
        rowView.setTitle(title);
    }

    private void checkPossibleVideoStatus() {
        mCompositeDisposable.add(mGetPossibleCurrentShow.execute(new GetPossibleCurrentShow.RequestValues(mShowListElements))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((responseValue -> {
                    Map<Integer, Integer> mPossibleCurrentShowsPosition = responseValue.getPossibleCurrentShowsPosition();
                    Set<Integer> showIds = mPossibleCurrentShowsPosition.keySet();
                    for (Integer id : showIds) {
                        int position = mPossibleCurrentShowsPosition.get(id);
                        checkVideoStatus(position, id, 10);
                    }
                }), throwable -> {

                }));

    }

    private void checkVideoStatus(int position, int showId, int secondsDelay) {
        mCompositeDisposable.add(Observable.timer(secondsDelay, SECONDS)
                .flatMap(lSecondsDelay -> mGetShowCurrentState.execute(new GetShowCurrentState.RequestValues(showId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((responseValue -> {
                    int isOnAir = responseValue.isIsOnAir();
                    if (isOnAir == 0) {
                        Show show = (Show) mShowListElements.get(position);
                        int isCurrent = show.getIsOnAir();
                        if (isCurrent == 1) {
                            show.setIsOnAir(0);
                            getAdapter().clearProgressTimer(position);
                            getAdapter().setProgressBarColor(position, Constants.Show.PROGRESS_STATUS_ENDED);
                            getAdapter().setProgressBarProgress(position, 10000);
                        }
                    }
                }), throwable -> {

                }));
    }

    public int getCurrentProgress(long lEndsIn, long lStartsIn) {
        long currentLong = new Date().getTime();
        long oneTick = (lEndsIn - lStartsIn) / 10000;
        long difference = (currentLong - lStartsIn) / oneTick;
        return (int) difference;
    }
}
