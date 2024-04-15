package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hku.course.utils.HttpPostRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
                String url = "http://1.14.241.22:8080/course/all";

                RequestBody requestBody = new FormBody.Builder()
                        .build();

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(MainPage.this, "Network Error", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String responseData = response.body().string();

                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
                        JsonArray dataArray = jsonObject.getAsJsonArray("data");

                        List<String> courseName = new ArrayList<>();
                        List<String> teacherName = new ArrayList<>();
                        List<String> rating = new ArrayList<>();

                        for (JsonElement element : dataArray) {
                            JsonObject courseObject = element.getAsJsonObject();
                            String courseNumber = courseObject.get("courseNumber").getAsString();
                            String courseTeacher = courseObject.get("courseTeacher").getAsString();
                            double courseScore = courseObject.get("courseScore").getAsDouble() / 20;

                            courseName.add(courseNumber);
                            teacherName.add(courseTeacher);
                            rating.add(String.valueOf(courseScore));
                        }

                        Intent intent = new Intent(MainPage.this, CoursePage.class);
                        intent.putExtra("username", username);
                        intent.putExtra("courseName", courseName.toArray(new String[0]));
                        intent.putExtra("teacherName", teacherName.toArray(new String[0]));
                        intent.putExtra("rating", rating.toArray(new String[0]));

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