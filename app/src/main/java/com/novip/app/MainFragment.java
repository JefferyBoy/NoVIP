package com.novip.app;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.novip.AppApplication;
import com.novip.Http;
import com.novip.R;
import com.novip.VipCheck;
import com.novip.model.Ad;
import com.novip.model.Platform;
import com.novip.view.IconTextView;
import com.novip.web.WebViewActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private IconTextViewAdapter iconTextViewAdapter;
    private Banner banner;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = view.findViewById(R.id.grid);
        if(iconTextViewAdapter == null){
            iconTextViewAdapter = new IconTextViewAdapter(getContext());
        }
        gridView.setAdapter(iconTextViewAdapter);
        gridView.setOnItemClickListener(this);
        banner = view.findViewById(R.id.banner);
        Http.getMainTabAD(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    final List<Ad> ads = JSON.parseArray(response.body().string(),Ad.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            show_ad(ads);
                        }
                    });
                }
            }
        });
        return view;
    }

    private void show_ad(List<Ad> ads){
        List<String> images = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for(int i=0;i<ads.size();i++){
            images.add(ads.get(i).getPicture_url());
            titles.add(ads.get(i).getName());
        }
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new BannerADImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片集合
        banner.setImages(images);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      /*  if(AppApplication.getInstance().getUser() == null || AppApplication.getInstance().getUser().getVip_end().getTime() - System.currentTimeMillis() < 0){
            //非VIP
            Toast.makeText(getContext(),"请联系管理员购买VIP",Toast.LENGTH_SHORT).show();
        }else {
            //视频入口选择
            Log.d("MainFragment","onItemClick:" + id);
            Intent intent = new Intent(getContext(),WebViewActivity.class);
            intent.putExtra("url",AppApplication.getInstance().getPlatforms().get(position).getAction_url());
            startActivity(intent);
        }*/

        //视频入口选择
        Log.d("MainFragment","onItemClick:" + id);
        Intent intent = new Intent(getContext(),WebViewActivity.class);
        intent.putExtra("url",AppApplication.getInstance().getPlatforms().get(position).getAction_url());
        startActivity(intent);
    }

    private class IconTextViewAdapter implements ListAdapter {
        private Context context;
        public IconTextViewAdapter(Context context){
            this.context = context;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return AppApplication.getInstance().getPlatforms().size();
        }

        @Override
        public Platform getItem(int position) {
            return AppApplication.getInstance().getPlatforms().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IconTextView ic ;
            if(convertView != null){
                ic = (IconTextView) convertView;
            }else{
                ic = new IconTextView(context);
            }
            ic.setImageText(getItem(position).getPicture_url(),getItem(position).getName());
            return ic;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }
    }
}
