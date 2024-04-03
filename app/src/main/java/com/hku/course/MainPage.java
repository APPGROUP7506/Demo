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
                String url = "https://68568bde.r3.cpolar.cn/course/all";

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
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Intent intent = new Intent(MainPage.this, CoursePage.class);

                        String responseData = response.body().string();
                        // Log.d("test", response.body().string());

                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
                        JsonArray dataArray = jsonObject.getAsJsonArray("data");

                        List<String> courseName = new ArrayList<String>();
                        List<String> teacherName = new ArrayList<String>();
                        List<String> rating = new ArrayList<String>();


                        CourseItem[] courseItems = new CourseItem[dataArray.size()];
                        for (int i = 0; i < dataArray.size(); i++) {
                            JsonElement element = dataArray.get(i);
                            String courseNumber = element.getAsJsonObject().get("courseNumber").getAsString();
                            String courseTeacher = element.getAsJsonObject().get("courseTeacher").getAsString();
                            double courseScore = element.getAsJsonObject().get("courseScore").getAsDouble() / 20;

                            courseName.add(courseNumber);
                            teacherName.add(courseTeacher);
                            rating.add(String.valueOf(courseScore));
                        }


                        // get the course detail information from here and sent to the next page
                        String[] courseName2 = courseName.toArray(new String[courseName.size()]);
                        String[] teacherName2 = teacherName.toArray(new String[teacherName.size()]);
                        String[] rating2 = rating.toArray(new String[rating.size()]);

                        intent.putExtra("username", username);
                        intent.putExtra("courseName", courseName2);
                        intent.putExtra("teacherName", teacherName2);
                        intent.putExtra("rating", rating2);

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