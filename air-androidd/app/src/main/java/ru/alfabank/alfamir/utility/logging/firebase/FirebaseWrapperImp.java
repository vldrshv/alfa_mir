package ru.alfabank.alfamir.utility.logging.firebase;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.di.qualifiers.AppContext;

public class FirebaseWrapperImp implements FirebaseWrapper {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Inject
    FirebaseWrapperImp(@AppContext Context context) {
        if (Constants.Companion.getBUILD_TYPE_PROD()) mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void logEvent(String tag, Bundle bundle) {
        if (mFirebaseAnalytics == null) return;
        mFirebaseAnalytics.logEvent(tag, bundle);
    }
}
