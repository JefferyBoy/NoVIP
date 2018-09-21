package com.novip.novip;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefernceUtils {

    private static SharedPrefernceUtils sp;

    private static SharedPreferences sharedPreferences;

    public static final String KEY_CHANNEL = "channel";

    public static final String TEST = "test";

    public static final String VIP_CODE = "VIP_CODE";

    private SharedPrefernceUtils(){}

    public static SharedPrefernceUtils getInstance(Context context){
        if(sp == null){
            sp = new SharedPrefernceUtils();
            sharedPreferences = context.getSharedPreferences("novip",Context.MODE_PRIVATE);
        }
        return sp;
    }

    public void putInt(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public int getInt(String key){
       return sharedPreferences.getInt(key,0);
    }

    public void putBoolean(String key,boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public void putString(String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }
}
