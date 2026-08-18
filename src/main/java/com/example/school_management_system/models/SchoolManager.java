package com.example.school_management_system.models;
import java.util.List;
import java.util.ArrayList;

public class SchoolManager {
    private List<Course> allCourses;
    private List<Student> allStudents;
    private String schoolName;

    public SchoolManager(String schoolName){
        allCourses = new ArrayList<>();
        allStudents = new ArrayList<>();
        this.schoolName = schoolName;
    }

    public String getSchoolName(){
        return this.schoolName;
    }

    /* Adding a new student in the database */
    public boolean addStudent(Student student){
        if(findStudentById(student.getId())== null){
            allStudents.add(student);
            return true;
        }
        return false;
    }

    /* Adding a new course in the course catalog */
    public boolean addCourse(Course course ){
        if(findCourseById(course.getCourseID())== null){
            allCourses.add(course);
            return true;
        }
        return false;
    }

    /* Finding a students data by their id */
    public Student findStudentById(String id){
        return allStudents.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /* Finding course data based on their id */
    public Course findCourseById( String id) {
        return allCourses.stream()
                .filter(course -> course.getCourseID().equals(id))
                .findFirst()
                .orElse(null);
    }

    /* Removing a student from the database and all the courses he/she is enrolled to */
    public boolean removeStudent( String id){
        Student st = findStudentById(id);
        if(st!=null){
            List<Course> studentSnapshot = new ArrayList<>(st.getCoursesEnrolled());
            for(Course cs : studentSnapshot){
                st.unenroll(cs);
            }
            allStudents.remove(st);
            return true;
        }
        return false;
    }

    /* Removing a course from the database (all the students enrolled in the course are deregistered as well) */
    public boolean removeCourse(String id){
        Course crs = findCourseById(id);
        if(crs!= null){
            List <Student> courseSnapshot = new ArrayList<>(crs.getEnrolledStudents());
            for(Student student : courseSnapshot){
                crs.deregister(student);
            }
            allCourses.remove(crs);
            return true;
        }
        return false;
    }


    /* Provide the total number of students enrolled in the school */
    public int getTotalNumberOfStudents(){
        return allStudents.size();
    }

    /* Provide total number of courses offered */
    public int getTotalNumberOfCourses(){
        return allCourses.size();
    }

    /* Enrolling a new Student inside a course */
    public boolean enrollStudentInCourse(String courseId,String studentId){
        Student student = findStudentById(studentId);
        Course course = findCourseById(courseId);
        if(student!=null && course!=null){
            student.enroll(course);
            return true;
        }
        return false;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(allStudents);
    }

    public List<Course> getAllCourses(){
        return new ArrayList<>(allCourses);
    }

}
