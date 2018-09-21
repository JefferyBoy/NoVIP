package com.novip.novip;

import android.content.Context;

public class VipChannel {

    public static String getVipChannel(Context context,int channel_id){

       return  context.getResources().getStringArray(R.array.decode_channel_url)[channel_id];
    }
}
