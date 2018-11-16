package com.novip.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.novip.Http;
import com.novip.R;
import com.novip.utils.DeviceUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisteActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        phone = findViewById(R.id.email);
        password = findViewById(R.id.password);
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String ph = phone.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if(ph.length() != 11){
            Toast.makeText(this,"手机号格式不对",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length() < 4){
            Toast.makeText(this,"密码长度要大于4",Toast.LENGTH_SHORT).show();
            return;
        }

        Http.registe(ph, pass,DeviceUtils.getUniqueId(this), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.code() == 200){
                            Toast.makeText(RegisteActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisteActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });

            }
        });
    }
}
