package com.hku.course;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class CourseViewAdapter extends ArrayAdapter<CourseItem> {

    private Context context;
    private OnButtonClickListener buttonClickListener;
    private int resource;

    public CourseViewAdapter(Context context, int resource, List<CourseItem> courses) {
        super(context, resource, courses);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.courseNameTextView = convertView.findViewById(R.id.courseNameTextView);
            viewHolder.teacherNameTextView = convertView.findViewById(R.id.teacherTextView);
            viewHolder.ratingBar = convertView.findViewById(R.id.ratingBar);
            viewHolder.button = convertView.findViewById(R.id.actionButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CourseItem course = getItem(position);

        viewHolder.courseNameTextView.setText(course.getCourseName());
        viewHolder.teacherNameTextView.setText(course.getTeacherName());
        viewHolder.ratingBar.setRating(Float.parseFloat(course.getRating()));

        // Set button click listener
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the new activity
                Intent intent = new Intent(context, CourseDetail.class);

                // Put the course data as extras in the intent
                intent.putExtra("courseName", course.getCourseName());

                // Start the new activity
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClick(int position);
    }

    private static class ViewHolder {
        TextView courseNameTextView;
        TextView teacherNameTextView;
        RatingBar ratingBar;
        Button button;
    }
}

