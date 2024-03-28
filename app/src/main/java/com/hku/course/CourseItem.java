package com.hku.course;

public class CourseItem {
    private String courseName;
    private String teacherName;
    private String rating;

    public CourseItem(String courseName, String teacherName, String rating){
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.rating = rating;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherName(){
        return teacherName;
    }

    public String getRating() {
        return rating;
    }
}
