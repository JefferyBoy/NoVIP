package com.novip;

import android.app.Application;
import android.util.Log;

import com.novip.model.Novip;
import com.novip.model.Platform;
import com.novip.model.User;
import com.novip.model.VParser;
import com.novip.model.Version;
import com.novip.utils.DeviceUtils;
import com.novip.utils.LogUtils;
import com.novip.utils.SharedPrefernceUtils;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Field;
import java.util.List;

public class AppApplication extends Application {
    public static final boolean DEBUG = false;
    private User user;
    private List<VParser> vParsers;
    private Version version;
    private static AppApplication instance;
    private Novip novip;
    private List<Platform> platforms;
    private String[] filterUrls;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //SP初始化
        SharedPrefernceUtils.getInstance(this);

        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        try {
            Class<?> aClass = Class.forName("com.umeng.commonsdk.UMConfigure");
            Field[] fs = aClass.getDeclaredFields();
            for (Field f:fs){
                Log.e("xxxxxx","ff="+f.getName()+"   "+f.getType().getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "5bc58c00b465f50c1700035e", "常规推广", UMConfigure.DEVICE_TYPE_PHONE,
                null);

    }

    public static AppApplication getInstance(){
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if(user!=null){
            SharedPrefernceUtils.getInstance(getBaseContext()).putString(
                    SharedPrefernceUtils.PHONE,user.getPhone()
            );
            SharedPrefernceUtils.getInstance(getBaseContext()).putString(
                    SharedPrefernceUtils.PS,user.getPassword()
            );
        }
        this.user = user;
    }

    public List<VParser> getvParsers() {
        return vParsers;
    }

    public void setvParsers(List<VParser> vParsers) {
        this.vParsers = vParsers;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Novip getNovip() {
        return novip;
    }

    public void setNovip(Novip novip) {
        this.novip = novip;
        Http.setHost(novip.getHost());
        Http.setPort(novip.getPort());
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public String[] getFilterUrls() {
        return filterUrls;
    }

    public void setFilterUrls(String[] filterUrls) {
        this.filterUrls = filterUrls;
    }
}
