package com.hku.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
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

public class CourseDetail extends AppCompatActivity {

    private TextView courseNameTextView;
    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitButton;
    private ListView lv_course_detail;

    private List<RemarkItem> remarkItemList = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        courseNameTextView = findViewById(R.id.courseNameTextView);
        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        submitButton = findViewById(R.id.submitButton);
        lv_course_detail = findViewById(R.id.lv_course_detail);

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String courseName = intent.getStringExtra("courseName");
        String[] userName = intent.getStringArrayExtra("userName");
        String[] userRating = intent.getStringArrayExtra("userRating");
        String[] detailRemark = intent.getStringArrayExtra("detailRemark");

        // String[] userName = {"user1", "user2", "user3"};
        // String[] userRating = {"3.5", "3.6", "3.7"};
        // String[] detailRemark = {"Remark 1", "Remark 2", "Remark 3"};

        courseNameTextView.setText(courseName);
        // Set up the ListView with peer remarks
        for (int i = 0; i < userName.length; i++) {
            RemarkItem item = new RemarkItem(userName[i], userRating[i], detailRemark[i]);
            remarkItemList.add(item);
        }
        RemarkAdapter remarkAdapter = new RemarkAdapter(this, remarkItemList);
        lv_course_detail.setAdapter(remarkAdapter);

        // Handle submit button click
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("username");
                float userRating = ratingBar.getRating();
                String comment = commentEditText.getText().toString();

                // handle the submit
                String url = "http://8q9020g440.vicp.fun/course/submit";

                RequestBody requestBody = new FormBody.Builder()
                        .add("userName", username)
                        .add("rating", String.valueOf(userRating))
                        .add("remark", comment)
                        .build();

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(CourseDetail.this, "Network Error", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Looper.prepare();

                        Toast.makeText(CourseDetail.this, "Submit Success", Toast.LENGTH_SHORT).show();
                        // handle the new remark
                        RemarkItem newRemark = new RemarkItem("Your Name", String.valueOf(userRating), comment);
                        remarkItemList.add(newRemark);
                        remarkAdapter.notifyDataSetChanged();
                        commentEditText.setText("Leave your comment here");
                        ratingBar.setRating(0);

                        Looper.loop();
                    }
                });
            }
        });
    }
}