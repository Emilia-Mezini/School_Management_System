package com;
import java.util.ArrayList;
import java.util.List;
public class Student {
    private String name;
    private String email;
    private String studentId;
    private List<Course> coursesEnrolled;


    public Student(String name, String email, String studentId){
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.coursesEnrolled = new ArrayList<>();
    }

    public String getId(){
        return this.studentId;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }
    public List<Course> getCoursesEnrolled(){
        return this.coursesEnrolled;
    }

    /* A Student might change his/her email */
    public void setEmail(String email){
        this.email = email;
    }

    /* A student should be able to enroll in a course */
    public void enroll(Course course) {
        if (!this.isEnrolled(course)) {
            coursesEnrolled.add(course);
            course.register(this);
        }
    }

    /* Check if a Student is already enrolled in a course */
    public boolean isEnrolled(Course course){
        return coursesEnrolled.contains(course);
    }

    /* A student should be able to unroll from a course */
    public void unenroll(Course course){
        if(this.isEnrolled(course)){
            coursesEnrolled.remove(course);
            course.deregister(this);
        }
    }

    public int getTotalCredits(){
        int totalCredits = 0;
        for(Course course : coursesEnrolled){
            totalCredits += course.getNumberOfCredits();
        }
        return totalCredits;
    }

    @Override
    public String toString(){
        return name + " (ID:" + studentId + ")";
    }

}
