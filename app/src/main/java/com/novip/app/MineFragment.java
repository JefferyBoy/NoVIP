package com.novip.app;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.novip.AppApplication;
import com.novip.Http;
import com.novip.R;
import com.novip.VipCheck;
import com.novip.model.User;
import com.novip.utils.ShareUtils;
import com.novip.view.VipCodeDialog;
import com.novip.utils.SharedPrefernceUtils;
import com.novip.view.SettingItemView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, VipCodeDialog.VerifyListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SettingItemView setVipCode;
    private SettingItemView setBuyCode;
    private SettingItemView setAddQQGroup;
    private SettingItemView setContactAdminQQ;
    private SettingItemView setShare;
    private SettingItemView setLogin;
    private SettingItemView setExitLogin;
    private SettingItemView set_change_password;
    private TextView vip_left;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        vip_left = view.findViewById(R.id.vip_left);
        setVipCode = view.findViewById(R.id.set_vipcode);
        setBuyCode = view.findViewById(R.id.set_buy);
        setAddQQGroup = view.findViewById(R.id.set_qqgroup);
        setContactAdminQQ = view.findViewById(R.id.set_adminqq);
        setShare = view.findViewById(R.id.set_share);
        setLogin = view.findViewById(R.id.set_login);
        setExitLogin = view.findViewById(R.id.set_exit_account);
        set_change_password = view.findViewById(R.id.set_change_password);
        setLogin.setOnClickListener(this);
        setExitLogin.setOnClickListener(this);
        setVipCode.setOnClickListener(this);
        setBuyCode.setOnClickListener(this);
        setAddQQGroup.setOnClickListener(this);
        setContactAdminQQ.setOnClickListener(this);
        setShare.setOnClickListener(this);
        setLogin.setText("登陆","");
        setExitLogin.setText("退出登陆","");
        setVipCode.setText("输入授权码","输入授权码激活VIP使用权限");
        setBuyCode.setText("购买授权码","购买成功后可激活永久VIP");
        setAddQQGroup.setText("NoVIP内部群","各种免费的工具软件，长按复制群号："+AppApplication.getInstance().getNovip().getQq_group());
        setContactAdminQQ.setText("联系管理员","联系管理员，长按复制管理员微信："+AppApplication.getInstance().getNovip().getAdmin_qq());
        setShare.setText("软件分享","");
        set_change_password.setText("修改密码","");

        setAddQQGroup.setOnLongClickListener(this);
        setContactAdminQQ.setOnLongClickListener(this);
        set_change_password.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void refreshVip(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(AppApplication.getInstance().getUser() == null){
                    setLogin.setVisibility(View.VISIBLE);
                    setExitLogin.setVisibility(View.GONE);
                }else {
                    setLogin.setVisibility(View.GONE);
                    setExitLogin.setVisibility(View.VISIBLE);
                }
                long v = AppApplication.getInstance().getUser() == null?0:AppApplication.getInstance().getUser().getVip_end().getTime();
                long duration = v - System.currentTimeMillis();
                if(duration <= 0){
                    //VIP过期
                    vip_left.setText("VIP剩余时间："+0+"天");
                }else {
                    long dt = 24*60*60*1000l;
                    int days = duration/dt > 0?(int)(duration/dt+1):1;
                    vip_left.setText("VIP剩余时间："+days+"天");
                }
            }
        });

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
        refreshVip();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_vipcode:

                new VipCodeDialog(getActivity(),this).show();
                break;
            case R.id.set_buy:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(AppApplication.getInstance().getNovip().getBuy_url());
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.set_qqgroup:
                Intent qqg = new Intent();
                qqg.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + "882519523"));
                // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(qqg);
                } catch (Exception e) {
                    Toast.makeText(getContext(),"未安装QQ或版本不支持",Toast.LENGTH_SHORT).show();
                    // 未安装手Q或安装的版本不支持
                }
                break;
            case R.id.set_adminqq:
//                if(isQQClientAvailable(getActivity())){
//                    final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=2277896242&version=1";
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
//                }else{
//                    Toast.makeText(getActivity(),"请安装QQ客户端",Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.set_share:
                ShareUtils.shareText(getContext(),"NoVIP视频APP","","爱奇艺、腾讯、优酷等VIP视频免费看，就在NoVIP视频APP，点这里下载："
                        +AppApplication.getInstance().getVersion().getUrl());
                /*ImageView imageView = new ImageView(getActivity());
                imageView.setImageResource(R.drawable.download_qrcode);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(300,300);
                imageView.setLayoutParams(params);
                new AlertDialog.Builder(getActivity())
                        .setView(imageView)
                        .setTitle("扫一扫下载")
                        .create()
                        .show();*/
                break;
            case R.id.set_login:
                startActivity(new Intent(getActivity(),LoginActivity.class));
                break;
            case R.id.set_exit_account:
                SharedPrefernceUtils.getInstance(getContext()).putString(SharedPrefernceUtils.PHONE,"");
                SharedPrefernceUtils.getInstance(getContext()).putString(SharedPrefernceUtils.PS,"");
                AppApplication.getInstance().setUser(null);
                refreshVip();
                startActivity(new Intent(getActivity(),LoginActivity.class));

                break;
            case R.id.set_change_password:
                if(AppApplication.getInstance().getUser() != null){
                    startActivity(new Intent(getActivity(),ChangePasswordActivity.class));
                }else {
                    Toast.makeText(getActivity(),"请登陆后操作",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        switch (v.getId()){
            case R.id.set_qqgroup:
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                // 将文本内容放到系统剪贴板里。
                cm.setText(AppApplication.getInstance().getNovip().getQq_group());
                Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_adminqq:
                cm.setText(AppApplication.getInstance().getNovip().getAdmin_qq());
                Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void relogin(){
        Http.login(AppApplication.getInstance().getUser().getPhone(),
                AppApplication.getInstance().getUser().getPassword(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code() == 200){
                            User user = JSON.parseObject(response.body().string(),User.class);
                            if(user != null){
                                AppApplication.getInstance().setUser(user);
                                refreshVip();
                            }
                        }
                    }
                });
    }
    @Override
    public void verify(final boolean b) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(b){
                    Toast.makeText(getContext(),"VIP授权成功",Toast.LENGTH_SHORT).show();
                    relogin();
                }else {
                    Toast.makeText(getContext(),"授权码不正确",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
