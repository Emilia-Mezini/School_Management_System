package com.example.school_management_system.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void newStudentHasNoCoursesEnrolled() {
        Student s = new Student("Test Person", "test@example.com", "12345");
        assertEquals(0, s.getCoursesEnrolled().size());
    }
}