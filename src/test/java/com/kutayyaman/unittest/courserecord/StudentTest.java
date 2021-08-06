package com.kutayyaman.unittest.courserecord;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*; //static import bu dikkat

public class StudentTest {

    @Test
    @DisplayName("Test every student must have an id, name and surname")
    void shouldCreateStudentWithIdNameAndSurnameWithGroupedAssertions() {
        Student student = new Student("1", "kutay", "yaman");

        Assertions.assertAll("Student's name check",
                () -> assertEquals("kutay", student.getName()),
                () -> assertEquals("kutay", student.getName(), "Student's name"),
                () -> assertNotEquals("kutayy", student.getName(), "Student's name")
        );

        Assertions.assertAll("Student's name character check",
                () -> assertTrue(student.getName().startsWith("k")),
                () -> assertTrue(student.getName().startsWith("k"), () -> "Student's name " + "starts with ku"),
                () -> assertFalse(() -> {
                    Student student1 = new Student("2", "maral", "yurdakul");
                    return student1.getName().endsWith("m");
                }, () -> "Student's name " + "ends with m")
        );

        assertAll(() -> {
                    final Student student2 = new Student("3", "nagihan", "yurdakul");

                    assertArrayEquals(new String[]{"kutay", "nagihan"}, Stream.of(student, student2).map(Student::getName).toArray());
                },
                () -> {
                    Student student3 = student;
                    final Student student4 = new Student("4", "Seher", "Yaman");

                    assertSame(student, student3);
                    assertNotSame(student3, student4);
                });
    }
}
