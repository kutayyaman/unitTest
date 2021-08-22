package com.kutayyaman.unittest.courserecord;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) //hep ayni ogrenci nesnesini kullanmak istedigimiz icin bunu yaptik yani bu student instance'nin bu test sinifindaki methodlarda degismesin istiyoruz yani her test icin yeniden olusturulmasin yani 1 tane olussun ve butun test methodlari onu kullansin demek oluyor eger bunu yapmazsak her test methodu icin yeniden sifirdan olusturulur.
public class StudentTestWithRepeatedTest implements TestLifecycleReporter {

    private Student student;

    @BeforeAll
    void setUp() {
        student = new Student("id1", "Ahmet", "Yilmaz");
    }

    @DisplayName("Add Course to Student")
    @RepeatedTest(value = 5, name = "{displayName} => Add one course to student and student has {currentRepetition} courses")
    void addCourseToStudent(RepetitionInfo repetitionInfo) {

        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course(String.valueOf(repetitionInfo.getCurrentRepetition())), new Semester());
        student.addCourse(lecturerCourseRecord);
        assertEquals(repetitionInfo.getCurrentRepetition(), student.getStudentCourseRecords().size());
    }
}
