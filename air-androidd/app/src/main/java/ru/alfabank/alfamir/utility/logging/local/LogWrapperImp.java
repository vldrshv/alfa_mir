package ru.alfabank.alfamir.utility.logging.local;

import android.util.Log;

import com.google.common.base.Strings;

import javax.inject.Inject;

import ru.alfabank.alfamir.BuildConfig;

public class LogWrapperImp implements LogWrapper {

    @Inject
    LogWrapperImp(){}

    @Override
    public void debug(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if(Strings.isNullOrEmpty(message)) message = "null";
            Log.d(tag, message);
        }
    }
}
