package com.agromall.android.imagedownloader;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        StrictMode.setVmPolicy(builder.build());
    }
}
