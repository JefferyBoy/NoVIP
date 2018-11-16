package com.novip.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.novip.Http;
import com.novip.R;

import org.jsoup.helper.StringUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText phone,password,new_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        phone = findViewById(R.id.email);
        password = findViewById(R.id.password);
        new_password = findViewById(R.id.new_password);
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone_s = phone.getText().toString().trim();
        String password_s = password.getText().toString().trim();
        String newPassword_s = new_password.getText().toString().trim();
        if(StringUtil.isBlank(phone_s) || StringUtil.isBlank(password_s)||StringUtil.isBlank(newPassword_s)){
            Toast.makeText(this,"输入内容不能为空",Toast.LENGTH_SHORT).show();
        }else {
            Http.changePassword(phone_s, password_s, newPassword_s, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code() == 200){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangePasswordActivity.this,"成功",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ChangePasswordActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
                    }
                }
            });
        }
    }
}
