package com.github.gripsack.android;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import net.danlew.android.joda.JodaTimeAndroid;

import timber.log.Timber;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;


public class GripsackApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
        enabledStrictMode();
    //    LeakCanary.install(this);
        Timber.plant(BuildConfig.DEBUG
                ? new Timber.DebugTree()
                : new CrashReportingTree());
        JodaTimeAndroid.init(this);
    }

    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());
        }
    }

    private class CrashReportingTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable throwable) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            Throwable t = throwable != null
                    ? throwable
                    : new Exception(message);

            // Firebase Crash Reporting
            FirebaseCrash.logcat(priority, tag, message);
            FirebaseCrash.report(t);
        }
    }
}
