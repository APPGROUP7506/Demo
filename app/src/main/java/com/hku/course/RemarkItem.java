package com.hku.course;

public class RemarkItem {
    private String userName;
    private String userRating;
    private String detailRemark;

    public RemarkItem(String userName, String rating, String detailRemark){
        this.userName = userName;
        this.userRating = rating;
        this.detailRemark = detailRemark;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getDetailRemark() {
        return detailRemark;
    }
}
