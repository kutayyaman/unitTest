package com.kutayyaman.unittest.courserecord.application;

import com.kutayyaman.unittest.courserecord.model.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;


public class StudentServiceImplTest {

    @Test
    void addCourse() {
        Course course = new Course("101");
        Semester semester = new Semester();
        final CourseService courseService = mock(CourseService.class);
        when(courseService.findCourse(any())).thenReturn(Optional.of(course));

        final LecturerService lecturerService = mock(LecturerService.class);
        Lecturer lecturer = mock(Lecturer.class);
        when(lecturer.lecturerCourseRecord(course, semester)).thenReturn(new LecturerCourseRecord(course, semester));

        when(lecturerService.findLecturer(course, semester)).thenReturn(Optional.of(lecturer));

        final StudentRepository studentRepository = mock(StudentRepository.class);
        Student studentKutay = new Student("id1", "Kutay", "Yaman");
        when(studentRepository.findById(anyString())).thenReturn(Optional.of(studentKutay)).thenThrow(new IllegalArgumentException("Can't find a student"));

        final StudentServiceImpl studentService = new StudentServiceImpl(courseService, lecturerService, studentRepository);
        studentService.addCourse("id1", course, semester);

        assertThatThrownBy(() -> studentService.findStudent("id1")).isInstanceOf(IllegalArgumentException.class);

        /*final Optional<Student> studentOptional = studentService.findStudent("id1");

        assertThat(studentOptional).as("Student")
                .isPresent()
                .get()
                .matches(student -> student.isTakeCourse(course));*/


    }
}
