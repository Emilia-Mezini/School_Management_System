package com.example.school_management_system.persistence;

import com.example.school_management_system.models.Course;
import com.example.school_management_system.models.SchoolManager;
import com.example.school_management_system.models.Student;

import java.util.ArrayList;
import java.util.List;

import static com.example.school_management_system.persistence.SchoolData.*;

/**
 * Bidirectional translator between the domain model (SchoolManager, Student, Course)
 * and the on-disk shape (SchoolData). Kept in the persistence package so neither the
 * domain nor the DTO needs to know about the other.
 */
public class SchoolDataMapper {

    private SchoolDataMapper() {
    }

    public static SchoolData toData(SchoolManager manager) {
        List<StudentData> students = manager.getAllStudents().stream()
                .map(SchoolDataMapper::toStudentData)
                .toList();

        List<CourseData> courses = manager.getAllCourses().stream()
                .map(SchoolDataMapper::toCourseData)
                .toList();

        List<Enrollment> enrollments = extractEnrollments(manager);

        return new SchoolData(manager.getSchoolName(), students, courses, enrollments);
    }

    public static SchoolManager toDomain(SchoolData data) {
        SchoolManager manager = new SchoolManager(data.schoolName());
        data.students().forEach(sd -> manager.addStudent(toStudent(sd)));
        data.courses().forEach(cd -> manager.addCourse(toCourse(cd)));
        data.enrollments().forEach(e -> manager.enrollStudentInCourse(e.courseId(), e.studentId()));
        return manager;
    }

    private static StudentData toStudentData(Student student) {
        return new StudentData(student.getId(), student.getName(), student.getEmail());
    }

    private static CourseData toCourseData(Course course) {
        return new CourseData(course.getCourseID(), course.getTitle(), course.getNumberOfCredits());
    }

    private static Student toStudent(StudentData data) {
        return new Student(data.name(), data.email(), data.studentId());
    }

    private static Course toCourse(CourseData data) {
        return new Course(data.courseId(), data.title(), data.numberOfCredits());
    }

    private static List<Enrollment> extractEnrollments(SchoolManager manager) {
        List<Enrollment> enrollments = new ArrayList<>();
        for (Student student : manager.getAllStudents()) {
            for (Course course : student.getCoursesEnrolled()) {
                enrollments.add(new Enrollment(student.getId(), course.getCourseID()));
            }
        }
        return enrollments;
    }
}
