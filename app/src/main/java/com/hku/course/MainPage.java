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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainPage.this, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent intent = new Intent(MainPage.this, CoursePage.class);

                        // get the course detail information from here and sent to the next page
                        String[] courseName = {"COMP 7506", "COMP 7507", "COMP 7508", "COMP 7509", "COMP 7510"};
                        String[] teacherName = {"T1", "T2", "T3", "T4", "T5"};
                        String[] rating = {"4.5", "4.0", "3.5", "3.0", "2.5"};

                        intent.putExtra("username", username);
                        intent.putExtra("courseName", courseName);
                        intent.putExtra("teacherName", teacherName);
                        intent.putExtra("rating", rating);

                        startActivity(intent);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Intent intent = new Intent(MainPage.this, CoursePage.class);

                        // get the course detail information from here and sent to the next page
                        String[] courseName = {"COMP 7506", "COMP 7507", "COMP 7508", "COMP 7509", "COMP 7510"};
                        String[] teacherName = {"T1", "T2", "T3", "T4", "T5"};
                        String[] rating = {"4.5", "4.0", "3.5", "3.0", "2.5"};

                        intent.putExtra("username", username);
                        intent.putExtra("courseName", courseName);
                        intent.putExtra("teacherName", teacherName);
                        intent.putExtra("rating", rating);

                        startActivity(intent);
                    }
                });
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this, "Logout Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}