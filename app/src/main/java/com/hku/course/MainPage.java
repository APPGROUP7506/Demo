package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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

public class MainPage extends AppCompatActivity {

    private ListView lv;
    private CourseViewAdapter adapter;
    private List<CourseItem> courseList = new ArrayList<>();

    private String[] courseName = {"COMP 7506", "COMP 7507", "COMP 7508", "COMP 7509", "COMP 7510"};
    private String[] teacherName = {"T1", "T2", "T3", "T4", "T5"};
    private String[] rating = {"4.5", "4.0", "3.5", "3.0", "2.5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

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
                // Create an intent to start the new activity
                Intent intent = new Intent(MainPage.this, CourseDetail.class);

                // Put the course data as extras in the intent
                intent.putExtra("courseName", course.getCourseName());
                intent.putExtra("rating", course.getRating());

                // Set the url
                String url = "http://8q9020g440.vicp.fun/course/detail";

                // Request for new data
                RequestBody requestBody = new FormBody.Builder()
                        .add("courseName", course.getCourseName())
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
                        Looper.prepare();

                        Intent intent = new Intent(MainPage.this, CourseDetail.class);

                        // get the course detail and remark item here from response
                        // intent.putExtras("courseDesp", ...)

                        startActivity(intent);
                        Looper.loop();
                    }
                });
            }
        });
    }
}