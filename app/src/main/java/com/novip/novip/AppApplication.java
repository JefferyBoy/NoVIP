package com.novip.novip;

import android.app.Application;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //SP初始化
        SharedPrefernceUtils.getInstance(this);

    }
}
