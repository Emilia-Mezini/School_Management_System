package com.example.school_management_system.persistence;

import java.util.List;
public record SchoolData(
        String schoolName,
        List<StudentData> students,
        List<CourseData> courses,
        List<Enrollment> enrollments
) {
    public record StudentData(String studentId, String name, String email) {}
    public record CourseData(String courseId, String title, int numberOfCredits) {}
    public record Enrollment(String studentId, String courseId) {}
}
