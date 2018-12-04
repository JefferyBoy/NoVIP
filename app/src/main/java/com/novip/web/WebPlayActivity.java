package com.novip.web;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.novip.R;
import com.novip.app.BaseActivity;

public class WebPlayActivity extends BaseActivity {
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_play);
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progress);
        setWebView();
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    private void setWebView(){

        WebSettings webSettings = webView.getSettings();
        //支持缩放，默认为true。
        webSettings.setSupportZoom(false);
        //调整图片至适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8");
        //设置自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        //设置浏览器标志
        //webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");

        //是否可以后退
        webView.canGoBack();
        webView.setWebViewClient(new NoADWebViewClent() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(final WebView view, String url) {
//                try {
//                    //延时一段时间等待动态iframe加载完成
//                    Thread.sleep(2000);
//                    view.loadUrl("javascript:window.js_interface.showSource('<head>'+"+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view,url);
            }
        });
        //加载进度,标题等
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });

    }



    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
