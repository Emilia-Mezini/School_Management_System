package com.example.school_management_system.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void newStudentHasNoCoursesEnrolled() {
        Student s = new Student("Test Person", "test@example.com", "12345");
        assertEquals(0, s.getCoursesEnrolled().size());
    }

    @Test
    void modifyingReturnedCourseListDoesNotAffectStudent() {
        Student s = new Student("Test Person", "test@example.com", "12345");
        Course c = new Course("C1", "Test Course", 3);
        s.enroll(c);

        List<Course> returned = s.getCoursesEnrolled();
        returned.clear();

        assertEquals(1, s.getCoursesEnrolled().size(),
                "Internal course list must not be affected by external mutation");
    }

    @Test
    void enrollingSameCourseTwiceOnlyRegistersOnce() {
        Student s = new Student("Test Person", "test@example.com", "12345");
        Course c = new Course("C1", "Test Course", 3);
        s.enroll(c);
        s.enroll(c);

        assertEquals(1, s.getCoursesEnrolled().size());
        assertEquals(1, c.getEnrolledStudents().size());
    }
}
