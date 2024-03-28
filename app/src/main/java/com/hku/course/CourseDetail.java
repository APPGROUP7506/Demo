package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CourseDetail extends AppCompatActivity {

    private String courseName;
    private String teacherName;
    private float rating;
    private TextView courseNameTextView;
    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitButton;
    private ListView lv_course_detail;

    private String[] userName = {"user1", "user2", "user3"};
    private String[] userRating = {"3.5", "3.6", "3.7"};
    private String[] detaliRemark = {"Remark 1", "Remark 2", "Remark 3"};

    private List<RemarkItem> remarkItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Intent intent = getIntent();

        courseName = intent.getStringExtra("courseName");
        teacherName = intent.getStringExtra("teacherName");
        rating = intent.getFloatExtra("rating", 0.0f);

        courseNameTextView = findViewById(R.id.courseNameTextView);
        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        submitButton = findViewById(R.id.submitButton);
        lv_course_detail = findViewById(R.id.lv_course_detail);

        courseNameTextView.setText(courseName);
        ratingBar.setRating(rating);

        // Set up the ListView with peer remarks
        remarkItemList = createRemarkItemList();
        RemarkAdapter remarkAdapter = new RemarkAdapter(this, remarkItemList);
        lv_course_detail.setAdapter(remarkAdapter);

        // Handle submit button click
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEditText.getText().toString();
                float userRating = ratingBar.getRating();
                RemarkItem newRemark = new RemarkItem("Your Name", String.valueOf(userRating), comment);
                remarkItemList.add(newRemark);
                remarkAdapter.notifyDataSetChanged();
                commentEditText.setText("");
                ratingBar.setRating(0);
            }
        });
    }

    private List<RemarkItem> createRemarkItemList() {
        List<RemarkItem> itemList = new ArrayList<>();
        for (int i = 0; i < userName.length; i++) {
            RemarkItem item = new RemarkItem(userName[i], userRating[i], detaliRemark[i]);
            itemList.add(item);
        }
        return itemList;
    }

}