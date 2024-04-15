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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hku.course.utils.HttpPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseDetail extends AppCompatActivity {

    private TextView courseNameTextView;
    // private TextView courseSummaryTextView;
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
        // courseSummaryTextView = findViewById(R.id.courseSummaryTextView);
        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        submitButton = findViewById(R.id.submitButton);
        lv_course_detail = findViewById(R.id.lv_course_detail);

        Intent intent = getIntent();

        // String description = intent.getStringExtra("description");
        String courseName = intent.getStringExtra("courseName");
        String[] userName = intent.getStringArrayExtra("userName");
        String[] userRating = intent.getStringArrayExtra("userRating");
        String[] detailRemark = intent.getStringArrayExtra("detailRemark");

        courseNameTextView.setText(courseName);
        // courseSummaryTextView.setText(description);
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
                double userRating = ratingBar.getRating() * 20;
                String comment = commentEditText.getText().toString();

                // handle the submit
                String url = "https://ecd311.r20.cpolar.top/course/comment";

                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                try {
                    json.put("courseNumber", courseName);
                    json.put("userName", username);
                    json.put("score", userRating);
                    json.put("userComment", comment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

                HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CourseDetail.this, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = null;
                        try {
                            responseData = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);

                        int submitStatus = jsonObject.get("code").getAsInt();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (submitStatus == 1){
                                    Toast.makeText(CourseDetail.this, "Submit Success", Toast.LENGTH_SHORT).show();
                                    RemarkItem newRemark = new RemarkItem(username, String.valueOf(userRating / 20), comment);
                                    remarkItemList.add(newRemark);
                                    remarkAdapter.notifyDataSetChanged();
                                    commentEditText.setText("");
                                    ratingBar.setRating(0);
                                }else{
                                    Toast.makeText(CourseDetail.this, "Fail to submit, please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}