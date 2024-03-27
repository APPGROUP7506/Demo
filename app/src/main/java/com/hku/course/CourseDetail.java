package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class CourseDetail extends AppCompatActivity {

    private String courseName;

    private String teacherName;

    private float rating;

    private TextView courseNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Intent intent = getIntent();

        courseName = intent.getStringExtra("courseName");
        teacherName = intent.getStringExtra("teacherName");
        rating = intent.getFloatExtra("rating", 0.0f);

        courseNameTextView = findViewById(R.id.courseNameTextView);

        courseNameTextView.setText(courseName);
    }
}
