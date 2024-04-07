package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hku.course.utils.HttpPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
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
        et_reg_password = findViewById(R.id.edit_reg_password);

        btn_reg_login = findViewById(R.id.btn_reg_login);
        btn_reg_register = findViewById(R.id.btn_reg_register);

        btn_reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://ecd311.r20.cpolar.top/user/register";

                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("email", et_reg_email.getText().toString());
                    json.put("phone", et_reg_phone.getText().toString());
                    json.put("hkuid", et_reg_hkuid.getText().toString());
                    json.put("username", et_reg_username.getText().toString());
                    json.put("password", et_reg_password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Looper.prepare();

                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);

                        int registerStatus = jsonObject.get("code").getAsInt();

                        if (registerStatus == 1){
                            Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "An unknown Error happened, please try again", Toast.LENGTH_SHORT).show();
                        }

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
                finish();
            }
        });
    }
}