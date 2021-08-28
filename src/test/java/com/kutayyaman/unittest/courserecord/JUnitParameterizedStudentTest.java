package com.kutayyaman.unittest.courserecord;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(CreateStudentConditionExtension.class)
//bundan dolayi su an Add createStudent calisacak cunku DropCourseConditionExtension icinde create tag'ini kontrol eden bir yapi var.
@DisplayName("Student Test with TestInfo and TestReporter Parameters")
public class JUnitParameterizedStudentTest {
    private Student student;

    public JUnitParameterizedStudentTest(TestInfo testInfo) {
        assertEquals("Student Test with TestInfo and TestReporter Parameters", testInfo.getDisplayName());
    }

    @BeforeEach
    void setStudent(TestInfo testInfo) {

        if (testInfo.getTags().contains("create")) {
            student = new Student("id1", "Ahmet", "Yilmaz");
        } else {
            student = new Student("id1", "Mehmet", "Yilmaz");
        }
    }

    @Test
    @DisplayName("Create Student")
    @Tag("create")
    void createStudent(TestInfo testInfo) {
        assertTrue(testInfo.getTags().contains("create"));
        assertEquals("Ahmet", student.getName());
    }

    @Test
    @DisplayName("Add Course to Student")
    @Tag("addCourse")
    void addCourseToStudent(TestReporter reporter) {

        reporter.publishEntry("startTime", LocalDateTime.now().toString());
        assertEquals("Mehmet", student.getName());
        reporter.publishEntry("endTime", LocalDateTime.now().toString());
    }
}
