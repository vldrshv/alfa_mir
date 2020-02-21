package ru.alfabank.alfamir.main.main_activity;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.main.main_activity.contract.MainActivityContract;
import ru.alfabank.alfamir.notification.domain.usecase.SendPushToken;

import javax.inject.Inject;

public class MainPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View mView;
    private boolean mIsFirstStart;
    private boolean mIsTokenSend;
    private SendPushToken mSendPushToken;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    MainPresenter(SendPushToken sendPushToken) {
        mSendPushToken = sendPushToken;
    }

    @Override
    public void takeView(MainActivityContract.View view) {
        mView = view;
        if (!mIsFirstStart) {
            mView.showHome();
            mIsFirstStart = true;
        }
        if (!mIsTokenSend) sendFBToken();
    }

    private void sendFBToken() {
        synchronized (this) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                String token = instanceIdResult.getToken();
                Log.d("Firebase", "token: " + token);
                mCompositeDisposable.add(mSendPushToken.execute(new SendPushToken.RequestValues(token))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(string -> {
                            mIsTokenSend = true;
                        }, throwable -> {

                        }));
            });

            FirebaseMessaging.getInstance().subscribeToTopic("PUSH_RC");
        }
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public MainActivityContract.View getView() {
        return null;
    }
}
