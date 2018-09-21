package com.novip.novip;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class VipCodeDialog  {
    private AlertDialog dialog;
    protected VipCodeDialog(@NonNull final Context context) {
        final EditText editText = new EditText(context);
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
                        if(editText.getText().toString().trim().equals(Native.stringFromJNI())){
                            Toast.makeText(context,"VIP授权成功",Toast.LENGTH_SHORT).show();
                            SharedPrefernceUtils.getInstance(context).putString(SharedPrefernceUtils.VIP_CODE,editText.getText().toString().trim());
                        }else{
                            Toast.makeText(context,"授权码不正确",Toast.LENGTH_SHORT).show();
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
}
