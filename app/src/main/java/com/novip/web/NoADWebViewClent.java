package com.novip.web;

import android.support.annotation.Nullable;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NoADWebViewClent extends WebViewClient{
    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        //判断是否是广告相关的资源链接
        if (!AdFilterTool.isAd(view.getContext(), request.getUrl().toString())) {
            //这里是不做处理的数据
            return super.shouldInterceptRequest(view, request);
        } else {
            //有广告的请求数据，我们直接返回空数据，注：不能直接返回null
            return new WebResourceResponse(null, null, null);
    }

    }
}
