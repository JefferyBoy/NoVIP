package com.novip.web;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.novip.AppApplication;
import com.novip.Http;
import com.novip.R;
import com.novip.app.BaseActivity;
import com.novip.model.VParser;
import com.novip.utils.SharedPrefernceUtils;
import com.novip.app.VipChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {
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
       // webView.loadUrl("http://api.bbbbbb.me/yunjx/?url=https://www.iqiyi.com/v_19rr7p19oc.html");
        //webView.loadUrl("http://jx.618g.com/?url=https://v.qq.com/x/cover/wi8e2p5kirdaf3j.html");

        if(AppApplication.getInstance().getvParsers() == null){
            checkVideoParser();
        }else {
            setChannel(SharedPrefernceUtils.getInstance(this).getInt(SharedPrefernceUtils.KEY_CHANNEL));
        }
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
        //是否可以后退
        webView.canGoBack();
        webView.setWebViewClient(new NoADWebViewClent() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("http") || url.startsWith("https")){
                    //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
                    view.loadUrl(url);
                    return true;
                }else {
                    //一些网页调用了第三方应用，比如支付宝支付，微信支付等，这些功能在webview中是会出现错误的
                    //可以在这里跳转到浏览器浏览url或者什么都不做
                    return true;
                }


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
        if(channel >=0 && channel < AppApplication.getInstance().getvParsers().size()){
            this.channel = channel;
            tv_channel.setText("当前线路：" + AppApplication.getInstance().getvParsers().get(channel).getNick_name());
            SharedPrefernceUtils.getInstance(this).putInt(SharedPrefernceUtils.KEY_CHANNEL, channel);
        }

    }
    private void checkVideoParser(){
        Http.getVParser(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response .code() == 200){
                    List<VParser> vParsers = JSON.parseArray(response.body().string(),VParser.class);
                    AppApplication.getInstance().setvParsers(vParsers);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.channel:
                //线路选择

                if(AppApplication.getInstance().getvParsers() == null){
                    checkVideoParser();
                    return;
                }
                final String[] lines = new String[AppApplication.getInstance().getvParsers().size()];
                for(int i=0;i<lines.length;i++){
                    String nn  = AppApplication.getInstance().getvParsers().get(i).getNick_name();
                    lines[i] = (nn==null?"线路"+i:nn);
                }
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
                Intent intent = new Intent(this,WebPlayActivity.class);
                intent.putExtra("url",VipChannel.getVipChannel(this, channel) + webView.getUrl());
                startActivity(intent);

                //startActivity(new Intent(this,PlayerActivity.class));
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
