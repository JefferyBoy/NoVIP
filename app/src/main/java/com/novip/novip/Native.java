package com.novip.novip;

public class Native {

    static {
        System.loadLibrary("native-lib");
    }

    public static native boolean verifyVip(String code);

    public static native String stringFromJNI();

}
