package com.novip;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.novip.app.LoginActivity;
import com.novip.app.MainActivity;
import com.novip.app.StartActivity;
import com.novip.model.AdUrl;
import com.novip.model.Novip;
import com.novip.model.Platform;
import com.novip.model.User;
import com.novip.model.VParser;
import com.novip.model.Version;
import com.novip.utils.DeviceUtils;
import com.novip.utils.LogUtils;
import com.novip.utils.SharedPrefernceUtils;
import com.umeng.analytics.MobclickAgent;

import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AppPrepare {

    private Activity context;
    private AppPrepareCallback cb;

    public interface AppPrepareCallback{
        void onPrepared();
    }

    private void isAppprepared(){
        if(AppApplication.getInstance().getVersion() != null &&
                AppApplication.getInstance().getvParsers() != null &&
                AppApplication.getInstance().getNovip() != null &&
                AppApplication.getInstance().getUser() != null &&
                AppApplication.getInstance().getPlatforms() != null &&
                AppApplication.getInstance().getFilterUrls() != null){
            cb.onPrepared();
        }
    }

    public void start(Activity context, @NonNull AppPrepareCallback cb){
        this.context = context;
        this.cb = cb;
        step1_checkServer();
    }
    
    private void step1_checkServer(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Novip novip;
                    long ll = SharedPrefernceUtils.getInstance(context).getLong("novip_date");
                    long period = System.currentTimeMillis() - ll;
                    if(period > 24*60*60*1000){
                        Response response = Http.getNovip();
                        String json = response.body().string();
                        LogUtils.D("下载",json);
                        novip = JSON.parseObject(json, Novip.class);
                        SharedPrefernceUtils.getInstance(context).putString("novip",json);
                        SharedPrefernceUtils.getInstance(context).putLong("novip_date",System.currentTimeMillis());

                    }else {
                        LogUtils.D("下载","使用本地数据");
                        String s = SharedPrefernceUtils.getInstance(context).getString("novip");
                        novip = JSON.parseObject(s, Novip.class);
                    }
                    if(novip != null){
                        AppApplication.getInstance().setNovip(novip);
                        step2_checkUpdate();
                    }else {
                        LogUtils.D("无法获取主机IP");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void step3_checkLogin(){
        if(AppApplication.getInstance().getUser() != null){
            step4_checkParser_Platform();
            return;
        }
        String phone = SharedPrefernceUtils.getInstance(context).getString(SharedPrefernceUtils.PHONE);
        String ps = SharedPrefernceUtils.getInstance(context).getString(SharedPrefernceUtils.PS);
        if(!StringUtil.isBlank(phone) &&! StringUtil.isBlank(ps)){
            //有登陆记录，再次登陆
            Http.login(phone, ps, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code() == 200){
                        User user = JSON.parseObject(response.body().string(), User.class);
                        LogUtils.D("登陆成功："+user.getPhone());
                        MobclickAgent.onProfileSignIn(user.getPhone());
                        AppApplication.getInstance().setUser(user);
                        step4_checkParser_Platform();
                    }else {
                        context.startActivity(new Intent(context,LoginActivity.class));
                    }
                }
            });
        }else {
            //未登陆，跳转到登陆
            context.startActivity(new Intent(context,LoginActivity.class));
        }
    }


    private void update(final Version version){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context)
                        .setTitle("软件更新")
                        .setMessage("有新版软件，请更新后运行")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse(version.getUrl());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                context.startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .create().show();
            }
        });
    }
    private void step2_checkUpdate(){
        Http.checkUpdate(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final int code = response.code();
                final String body = response.body().string();if(code == 200){
                    Version version = JSON.parseObject(body,Version.class);
                    AppApplication.getInstance().setVersion(version);

                    if(version != null && version.getVersion_code() != DeviceUtils.getAppVersionCode(context)){
                        update(version);
                    }else {
                        //不需要更新时，检测登陆和视频解析API
                        step3_checkLogin();
                    }
                }
            }
        });
    }

    public void step4_checkParser_Platform(){
        step4_checkVideoParser();
        step4_checkPlatforms();
        step4_getAdFilter();
    }

    private void step4_checkVideoParser(){
        Http.getVParser(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response .code() == 200){
                    List<VParser> vParsers = JSON.parseArray(response.body().string(),VParser.class);
                    AppApplication.getInstance().setvParsers(vParsers);
                    isAppprepared();
                }
            }
        });
    }

    private void step4_checkPlatforms(){
        Http.getPlatforms(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    List<Platform> platforms = JSON.parseArray(response.body().string(),Platform.class);
                    if(platforms != null && !platforms.isEmpty()){
                        AppApplication.getInstance().setPlatforms(platforms);
                        isAppprepared();
                    }
                }
            }
        });
    }

    private void step4_getAdFilter(){
        Http.getAdUrl(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    List<AdUrl> urls = JSON.parseArray(response.body().string(),AdUrl.class);
                    if(urls != null && !urls.isEmpty()){
                        String[] ads = new String[urls.size()];
                        int i = 0;
                        for(AdUrl url: urls){
                            ads[i++] = url.getUrl();
                        }
                        AppApplication.getInstance().setFilterUrls(ads);
                        isAppprepared();
                    }
                }
            }
        });
    }
}
