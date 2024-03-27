package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hku.course.utils.HttpPostRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_reg_email;

    private EditText et_reg_phone;

    private EditText et_reg_hkuid;

    private EditText et_reg_username;

    private EditText et_reg_password;

    private Button btn_reg_register;

    private Button btn_reg_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerPage();
    }

    private void registerPage(){
        //绑定控件
        et_reg_email = findViewById(R.id.edit_reg_email);
        et_reg_phone = findViewById(R.id.edit_reg_phone);
        et_reg_hkuid = findViewById(R.id.edit_reg_hkuid);
        et_reg_username = findViewById(R.id.edit_reg_username);
        et_reg_password = findViewById(R.id.edit_password);

        btn_reg_login = findViewById(R.id.btn_reg_login);
        btn_reg_register = findViewById(R.id.btn_reg_register);

        btn_reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://127.0.0.1:8080/user/lgoin";

                //请求传入的参数
                RequestBody requestBody = new FormBody.Builder()
                        .add("email", et_reg_email.getText().toString())
                        .add("phone", et_reg_phone.getText().toString())
                        .add("hkuid", et_reg_hkuid.getText().toString())
                        .add("username", et_reg_username.getText().toString())
                        .add("password", et_reg_password.getText().toString())
                        .build();

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();

                        //假设登录成功，此处应该放在onResponse函数中，返回注册成功信息
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);

                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });
            }
        });

        btn_reg_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}