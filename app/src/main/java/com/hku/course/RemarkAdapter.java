package com.hku.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class RemarkAdapter extends ArrayAdapter<RemarkItem> {
    private LayoutInflater inflater;

    public RemarkAdapter(Context context, List<RemarkItem> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.remark_item, parent, false);
            holder = new ViewHolder();
            holder.userNameTextView = convertView.findViewById(R.id.userNameTextView);
            holder.userRatingBar = convertView.findViewById(R.id.userRatingBar);
            holder.userCommentTextView = convertView.findViewById(R.id.userCommentTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RemarkItem item = getItem(position);

        holder.userNameTextView.setText(item.getUserName());
        holder.userRatingBar.setRating(Float.parseFloat(item.getUserRating()));
        holder.userCommentTextView.setText(item.getDetailRemark());

        return convertView;
    }

    private static class ViewHolder {
        TextView userNameTextView;
        RatingBar userRatingBar;
        TextView userCommentTextView;
    }
}