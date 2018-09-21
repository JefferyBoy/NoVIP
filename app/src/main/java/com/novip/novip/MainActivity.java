package com.novip.novip;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
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
    }

    @Override
    public void onPageScrollStateChanged(int i) {

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
