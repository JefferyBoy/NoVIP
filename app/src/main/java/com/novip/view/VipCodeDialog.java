package com.novip.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.novip.AppApplication;
import com.novip.Http;
import com.novip.VipCheck;
import com.novip.app.ChangePasswordActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class VipCodeDialog implements Callback {
    private AlertDialog dialog;
    private VerifyListener listener;
    public VipCodeDialog(@NonNull final Context context, final VerifyListener listener) {
        final EditText editText = new EditText(context);
        this.listener = listener;
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setHint("授权码");
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        editText.setPadding(0,30,0,30);
        dialog = new AlertDialog.Builder(context)
                .setTitle("输入授权码")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(AppApplication.getInstance().getUser() == null){
                            Toast.makeText(context,"请登陆后操作",Toast.LENGTH_SHORT).show();
                        }else {
                            Http.checkCode(AppApplication.getInstance().getUser().getPhone(),editText.getText().toString().trim(),VipCodeDialog.this);
                        }
                    }
                })
                .setNegativeButton("取消",null)
                .create();
    }

    public void show(){
        if(dialog != null){
            dialog.show();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {

        if(listener != null){
            listener.verify(false);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response.code() == 200){
            if(listener != null){
                listener.verify(true);
            }
        }else {
            if(listener != null){
                listener.verify(false);
            }
        }
    }

    public interface VerifyListener{
        void verify(boolean b);
    }
}
