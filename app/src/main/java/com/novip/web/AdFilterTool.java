package com.novip.web;

import android.content.Context;
import android.content.res.Resources;

import com.alibaba.fastjson.JSON;
import com.novip.AppApplication;
import com.novip.Http;
import com.novip.R;
import com.novip.model.AdUrl;
import com.novip.model.Platform;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
*  广告过滤规则，参考360过滤：http://rules.wd.360.cn/
* */
public class AdFilterTool {

    public static boolean isAd(Context context, String url) {
        if(AppApplication.getInstance().getFilterUrls() != null && AppApplication.getInstance().getFilterUrls().length>0){
            for (String adUrl : AppApplication.getInstance().getFilterUrls() ) {
                if (url.contains(adUrl)) {
                    return true;
                }
            }
        }
        return false;
    }
}