package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hku.course.utils.HttpPostRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainPage extends AppCompatActivity {

    private Button btn_view_courses;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        String username = getIntent().getStringExtra("username");

        btn_view_courses = findViewById(R.id.btn_view_courses);
        btn_logout = findViewById(R.id.btn_logout);

        btn_view_courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://8q9020g440.vicp.fun/course/all";

                RequestBody requestBody = new FormBody.Builder()
                        .build();

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(MainPage.this, "Network Error", Toast.LENGTH_SHORT).show();
                        ///////////
                        Intent intent = new Intent(MainPage.this, CoursePage.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        //////////
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // response.body().toString();
                        Intent intent = new Intent(MainPage.this, CoursePage.class);
                        intent.putExtra("username", username);

                        // get the course detail information from here and sent to the next page

                        startActivity(intent);
                        Looper.loop();
                    }
                });
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Looper.prepare();
                Toast.makeText(MainPage.this, "Logout Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPage.this, MainActivity.class);
                startActivity(intent);
                finish();
                Looper.loop();
            }
        });
    }
}

