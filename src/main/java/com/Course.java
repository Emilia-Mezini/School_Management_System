package com;
import java.util.ArrayList;
import java.util.List;
public class Course {
    private final String course_ID;
    private String title;
    private int numberOfCredits;
    private List<Student> enrolledStudents;

    public Course(String course_ID, String title, int numberOfCredits){
        this.course_ID = course_ID;
        this.title = title;
        this.numberOfCredits = numberOfCredits;
        enrolledStudents = new ArrayList<>();
    }

    public String getCourseID(){
        return this.course_ID;
    }

    public String getTitle(){
        return this.title;
    }

    public int getNumberOfCredits(){
        return this.numberOfCredits;
    }

    public List<Student> getEnrolledStudents(){ return this.enrolledStudents; }

    /* Courses name can be updated in the future*/
    public void setTitle(String title){
        this.title = title;
    }

    /* To meet new standards the credits number for a course can change */
    public void setNumberOfCredits(int numberOfCredits){
        this.numberOfCredits = numberOfCredits;
    }

    /* A course should have registered students */
    public void register(Student student){
        if(!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            student.enroll(this);
        }
    }

    public void deregister(Student student){
        if(enrolledStudents.contains(student)){
            enrolledStudents.remove(student);
            student.unenroll(this);
        }
    }

    /* We would like to print out the names of the course participants */
    public String printParticipantsNames(){
        StringBuilder sb = new StringBuilder();
        for( Student student: enrolledStudents ){
            sb.append(student.getName()).append(",");
        }
        return sb.toString();
    }
}
