package com.example.school_management_system.persistence;

import com.example.school_management_system.models.Course;
import com.example.school_management_system.models.SchoolManager;
import com.example.school_management_system.models.Student;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

    class SchoolDataMapperTest {

        @Test
        void toDataCapturesSchoolNameStudentsCoursesAndEnrollments() {
            SchoolManager manager = new SchoolManager("Test School");
            Student alice = new Student("Alice", "alice@example.com", "S1");
            Student bob = new Student("Bob", "bob@example.com", "S2");
            Course math = new Course("C1", "Math", 5);
            manager.addStudent(alice);
            manager.addStudent(bob);
            manager.addCourse(math);
            alice.enroll(math);
            bob.enroll(math);

            SchoolData data = SchoolDataMapper.toData(manager);

            assertEquals("Test School", data.schoolName());
            assertEquals(2, data.students().size());
            assertEquals(1, data.courses().size());
            assertEquals(2, data.enrollments().size());
        }

        @Test
        void toDomainReconstructsSchoolManagerFromData() {
            SchoolData data = new SchoolData(
                    "Test School",
                    java.util.List.of(
                            new SchoolData.StudentData("S1", "Alice", "alice@example.com")
                    ),
                    java.util.List.of(
                            new SchoolData.CourseData("C1", "Math", 5)
                    ),
                    java.util.List.of(
                            new SchoolData.Enrollment("S1", "C1")
                    )
            );

            SchoolManager manager = SchoolDataMapper.toDomain(data);

            assertEquals("Test School", manager.getSchoolName());
            assertEquals(1, manager.getTotalNumberOfStudents());
            assertEquals(1, manager.getTotalNumberOfCourses());

            Student alice = manager.findStudentById("S1");
            assertNotNull(alice);
            assertEquals("Alice", alice.getName());
            assertEquals("alice@example.com", alice.getEmail());
            assertEquals(1, alice.getCoursesEnrolled().size());
            assertEquals("C1", alice.getCoursesEnrolled().get(0).getCourseID());
        }

        @Test
        void roundTripPreservesEverything() {
            SchoolManager original = new SchoolManager("Original School");
            Student alice = new Student("Alice", "alice@example.com", "S1");
            Student bob = new Student("Bob", "bob@example.com", "S2");
            Course math = new Course("C1", "Math", 5);
            Course cs = new Course("C2", "Computer Science", 8);
            original.addStudent(alice);
            original.addStudent(bob);
            original.addCourse(math);
            original.addCourse(cs);
            alice.enroll(math);
            alice.enroll(cs);
            bob.enroll(math);

            SchoolManager reconstructed = SchoolDataMapper.toDomain(SchoolDataMapper.toData(original));

            assertEquals(original.getSchoolName(), reconstructed.getSchoolName());
            assertEquals(original.getTotalNumberOfStudents(), reconstructed.getTotalNumberOfStudents());
            assertEquals(original.getTotalNumberOfCourses(), reconstructed.getTotalNumberOfCourses());

            Student reconAlice = reconstructed.findStudentById("S1");
            assertEquals(2, reconAlice.getCoursesEnrolled().size());
            assertEquals(13, reconAlice.getTotalCredits());

            Course reconMath = reconstructed.findCourseById("C1");
            assertEquals(2, reconMath.getEnrolledStudents().size());
        }
    }

