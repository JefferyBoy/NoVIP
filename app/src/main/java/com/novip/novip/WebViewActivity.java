package com.novip.novip;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webView;
    TextView tv_title;
    TextView tv_channel;
    ProgressBar progressBar;
    private int channel = 0;//解析通道号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webview);
        tv_title = findViewById(R.id.title);
        tv_channel = findViewById(R.id.channel);
        progressBar = findViewById(R.id.progress);
        findViewById(R.id.image).setOnClickListener(this);
        findViewById(R.id.channel).setOnClickListener(this);
        tv_channel.setOnClickListener(this);
        setWebView();
        webView.loadUrl(getIntent().getStringExtra("url"));
        setChannel(SharedPrefernceUtils.getInstance(this).getInt(SharedPrefernceUtils.KEY_CHANNEL));
    }

    private void setWebView() {
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
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //允许JS
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //是否可以后退
        webView.canGoBack();

        //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //加载进度,标题等
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                tv_title.setText(title);
            }
        });

    }

    public void setChannel(int channel) {
        if (channel < 0 || channel > getResources().getStringArray(R.array.decode_channel_text).length) {
            Log.d("WebViewActivity", "线路不对");
            return;
        }
        this.channel = channel;
        tv_channel.setText("当前线路：" + getResources().getStringArray(R.array.decode_channel_text)[channel]);
        SharedPrefernceUtils.getInstance(this).putInt(SharedPrefernceUtils.KEY_CHANNEL, channel);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.channel:
                //线路选择
                final String[] lines = getResources().getStringArray(R.array.decode_channel_text);
                AlertDialog dialog = new AlertDialog.Builder(this, 0)
                        .setTitle("线路选择")
                        .setItems(lines, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setChannel(which);
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                break;
            case R.id.close:
                //关闭页面
                finish();
                break;
            case R.id.image:
                //播放按钮
                webView.loadUrl(VipChannel.getVipChannel(this, channel) + webView.getUrl());
                break;
        }
    }

    //按返回键回退网页
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
