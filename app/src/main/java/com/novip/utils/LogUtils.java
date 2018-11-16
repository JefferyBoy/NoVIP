package com.novip.utils;

import android.util.Log;

import com.novip.AppApplication;

public class LogUtils {

    public static final String TAG = "com.novip";


    public static void D(String str){
        if(AppApplication.DEBUG){
            Log.d(TAG,str);
        }
    }

    public static void D(String tag,String str){
        if(AppApplication.DEBUG){
            Log.d(tag,str);
        }
    }

}
