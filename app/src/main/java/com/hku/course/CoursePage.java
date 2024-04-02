package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


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
                // Set the url
                String url = "http://8q9020g440.vicp.fun/course/detail";

                // Request for new data
                RequestBody requestBody = new FormBody.Builder()
                        .add("courseName", course.getCourseName())
                        .build();

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CoursePage.this, "Network Error", Toast.LENGTH_SHORT).show();
                                ////////////////
                                Intent intent = new Intent(CoursePage.this, CourseDetail.class);

                                String[] userName = {"user1", "user2", "user3"};
                                String[] userRating = {"3.5", "3.6", "5.0"};
                                String[] detailRemark = {"Remark 1", "Remark 2", "Remark 3"};

                                intent.putExtra("username", username);
                                intent.putExtra("courseName", course.getCourseName());
                                intent.putExtra("userName", userName);
                                intent.putExtra("userRating", userRating);
                                intent.putExtra("detailRemark", detailRemark);

                                startActivity(intent);
                                //////////////////
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CoursePage.this, "Network Error", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CoursePage.this, CourseDetail.class);

                                String[] userName = {"user1", "user2", "user3"};
                                String[] userRating = {"3.5", "3.6", "3.7"};
                                String[] detailRemark = {"Remark 1", "Remark 2", "Remark 3"};

                                intent.putExtra("username", username);
                                intent.putExtra("courseName", course.getCourseName());
                                intent.putExtra("userName", userName);
                                intent.putExtra("userRating", userRating);
                                intent.putExtra("detailRemark", detailRemark);

                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }
}