package com.yuan.testmemory;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        LeakCanary.install(this);
    }
}
