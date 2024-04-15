package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hku.course.utils.HttpPostRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CoursePage extends AppCompatActivity {

    private ListView lv;
    private CourseViewAdapter adapter;
    private List<CourseItem> courseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        String username = getIntent().getStringExtra("username");
        String[] courseName = getIntent().getStringArrayExtra("courseName");
        String[] teacherName = getIntent().getStringArrayExtra("teacherName");
        String[] rating = getIntent().getStringArrayExtra("rating");

        // Create the course list
        for (int i = 0; i < courseName.length; i++) {
            CourseItem course = new CourseItem(courseName[i], teacherName[i], rating[i]);
            courseList.add(course);
        }

        // Create the adapter
        adapter = new CourseViewAdapter(this, R.layout.course_item, courseList);

        // Set the adapter to the ListView
        lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);

        // Set button click listener
        adapter.setOnButtonClickListener(new CourseViewAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int position) {
                CourseItem course = courseList.get(position);

                String url = "https://ecd311.r20.cpolar.top/course/detail/" + course.getCourseName();

                RequestBody requestBody = new FormBody.Builder()
                        .build();

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(CoursePage.this, "Network Error", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String responseData = response.body().string();

                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
                        JsonArray dataArray = jsonObject.getAsJsonArray("data");

                        List<String> userName = new ArrayList<>();
                        List<String> userRating = new ArrayList<>();
                        List<String> detailRemark = new ArrayList<>();

                        for (JsonElement element : dataArray) {
                            JsonObject dataObject = element.getAsJsonObject();
                            String userId = dataObject.get("userName").getAsString();
                            double score = dataObject.get("score").getAsDouble() / 20;
                            String userComment = dataObject.get("userComment").getAsString();

                            userName.add(userId);
                            userRating.add(String.valueOf(score));
                            detailRemark.add(userComment);
                        }

                        // realization of the description
                        // String description = "This is a description of this course";

                        Intent intent = new Intent(CoursePage.this, CourseDetail.class);

                        intent.putExtra("username", username);
                        intent.putExtra("courseName", course.getCourseName());
                        // intent.putExtra("description", description);
                        intent.putExtra("userName", userName.toArray(new String[0]));
                        intent.putExtra("userRating", userRating.toArray(new String[0]));
                        intent.putExtra("detailRemark", detailRemark.toArray(new String[0]));

                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = "https://ecd311.r20.cpolar.top/course/all";

        RequestBody requestBody = new FormBody.Builder()
                .build();

        HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CoursePage.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        courseList.clear();

                        for (int i = 0; i < courseName.size(); i++) {
                            CourseItem course = new CourseItem(courseName.get(i), teacherName.get(i), rating.get(i));
                            courseList.add(course);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}