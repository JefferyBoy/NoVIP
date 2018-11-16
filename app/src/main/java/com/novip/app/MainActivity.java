package com.novip.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.novip.AppApplication;
import com.novip.Http;
import com.novip.R;
import com.novip.R;
import com.novip.model.User;
import com.novip.model.VParser;
import com.novip.model.Version;
import com.novip.utils.DeviceUtils;
import com.novip.utils.LogUtils;
import com.novip.utils.SharedPrefernceUtils;
import com.umeng.analytics.MobclickAgent;

import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements
        RadioGroup.OnCheckedChangeListener,
        OnFragmentInteractionListener, ViewPager.OnPageChangeListener {

    // Used to load the 'native-lib' library on application startup.

    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private RadioButton radioButton0;
    private RadioButton radioButton1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.ll);
        viewPager = findViewById(R.id.viewpager);
        radioButton0 = findViewById(R.id.radio0);
        radioButton1 = findViewById(R.id.radio1);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);


        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radio0:
                viewPager.setCurrentItem(0);
                radioButton0.setTextColor(getResources().getColor(android.R.color.white));
                radioButton1.setTextColor(getResources().getColor(android.R.color.black));
                break;
            case R.id.radio1:
                viewPager.setCurrentItem(1);
                radioButton1.setTextColor(getResources().getColor(android.R.color.white));
                radioButton0.setTextColor(getResources().getColor(android.R.color.black));
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        //页面切换调研
        switch (i){
            case 0:
                radioButton0.setTextColor(getResources().getColor(android.R.color.white));
                radioButton1.setTextColor(getResources().getColor(android.R.color.black));
                radioButton0.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                radioButton1.setBackgroundColor(getResources().getColor(android.R.color.white));
                break;
             case 1:
                radioButton1.setTextColor(getResources().getColor(android.R.color.white));
                radioButton0.setTextColor(getResources().getColor(android.R.color.black));
                radioButton1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                radioButton0.setBackgroundColor(getResources().getColor(android.R.color.white));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        onPageSelected(0);

        if (Build.VERSION.SDK_INT >= 23) {
            int checkWriteStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkWriteStoragePermission != PackageManager.PERMISSION_GRANTED){
                //没有权限，需要申请
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }else{
            //有权限
        }



}

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();// 创建Intent对象
            intent.setAction(Intent.ACTION_MAIN);// 设置Intent动作
            intent.addCategory(Intent.CATEGORY_HOME);// 设置Intent种类
            startActivity(intent);// 将Intent传递给Activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter{

        private List<Fragment> fragmentList;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
            MainFragment f = new MainFragment();
            MineFragment m = new MineFragment();
            fragmentList.add(f);
            fragmentList.add(m);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
