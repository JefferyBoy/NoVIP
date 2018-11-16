package com.novip.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.novip.AppApplication;
import com.novip.AppPrepare;
import com.novip.Http;
import com.novip.R;
import com.novip.model.Novip;
import com.novip.model.Platform;
import com.novip.model.User;
import com.novip.model.VParser;
import com.novip.model.Version;
import com.novip.utils.DeviceUtils;
import com.novip.utils.LogUtils;
import com.novip.utils.SharedPrefernceUtils;
import com.novip.view.CountDownView;
import com.umeng.analytics.MobclickAgent;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
*  APP准备工作：
*  1.获取主机IP，端口等基本信息
*  2.检查更新（有更新时强制更新，无更新时到第3步）
*  3.登陆（成功到4，失败到登陆界面）
*  4.获取APP中需要使用到的数据（平台信息、解析线路等）
*
* */
public class StartActivity extends AppCompatActivity implements CountDownView.OnCountDownFinishListener, AppPrepare.AppPrepareCallback {

    private CountDownView countDownView;
    private boolean appPrepared = false;
    private boolean countDownFinished =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        countDownView = findViewById(R.id.cdv);
        countDownView.setAddCountDownListener(this);
        countDownView.startCountDown();
    }

    private void toast(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(),"无法连接到服务器",Toast.LENGTH_SHORT).show();
                //finish();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        new AppPrepare().start(this,this);
    }

    @Override
    public void countDownFinished() {
        countDownFinished = true;
        if(appPrepared && countDownFinished){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onPrepared() {
        appPrepared = true;
        if(appPrepared && countDownFinished){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}
