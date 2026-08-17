package com.example.school_management_system.models;
import com.example.school_management_system.models.Course;
import com.example.school_management_system.models.Student;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseTest {
    @Test
    void newCourseHasNoEnrolledStudents() {
        Course c = new Course("C1", "Test Course", 3);
        assertEquals(0, c.getEnrolledStudents().size());
    }

    @Test
    void modifyingReturnedStudentListDoesNotAffectCourse() {
        Course c = new Course("C1", "Test Course", 3);
        Student s = new Student("Test Person", "test@example.com", "12345");
        c.register(s);

        List<Student> returned = c.getEnrolledStudents();
        returned.clear();

        assertEquals(1, c.getEnrolledStudents().size(),
                "Internal student list must not be affected by external mutation");
    }

    @Test
    void deregisterRemovesStudentFromBothSides() {
        Course c = new Course("C1", "Test Course", 3);
        Student s = new Student("Test Person", "test@example.com", "12345");
        c.register(s);
        c.deregister(s);

        assertEquals(0, c.getEnrolledStudents().size());
        assertEquals(0, s.getCoursesEnrolled().size());
    }
}

