package com.novip.app;

import android.content.Context;

import com.novip.AppApplication;
import com.novip.R;

public class VipChannel {

    public static String getVipChannel(Context context,int channel_id){

      // return  context.getResources().getStringArray(R.array.decode_channel_url)[channel_id];
        return AppApplication.getInstance().getvParsers().get(channel_id).getUrl();
    }
}
