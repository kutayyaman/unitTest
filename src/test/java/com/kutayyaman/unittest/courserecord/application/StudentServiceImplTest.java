package com.kutayyaman.unittest.courserecord.application;

import com.kutayyaman.unittest.courserecord.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


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
        when(studentRepository.findById(anyString())).thenReturn(Optional.of(studentKutay)).thenThrow(new IllegalArgumentException("Can't find a student")).thenReturn(Optional.of(studentKutay));

        final StudentServiceImpl studentService = new StudentServiceImpl(courseService, lecturerService, studentRepository);
        studentService.addCourse("id1", course, semester);

        assertThatThrownBy(() -> studentService.findStudent("id1")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Student> studentOptional = studentService.findStudent("id1");

        assertThat(studentOptional).as("Student")
                .isPresent()
                .get()
                .matches(student -> student.isTakeCourse(course));

        verify(courseService).findCourse(course); //findCourse methodu course parametresi ile 1 defa cagirilmis olmasi gerekiyor onu kontrol ediyor burda
        verify(courseService,times(1)).findCourse(course);//buda yukardakiyle ayni sadece kac kere cagrildigini parametre olarak verdik eger vermezsek default olarak 1 yapiyor zaten
        verify(courseService,atLeast(1)).findCourse(course);//en az 1 defa cagrilmis diye kontrol ettik
        verify(courseService,atMost(1)).findCourse(course); // en fazla 1 kere cagrilmis diye kontrol ettik

        verify(studentRepository,times(3)).findById("id1");

        verify(lecturerService).findLecturer(any(Course.class),any(Semester.class));

        verify(lecturer).lecturerCourseRecord(argThat(argument -> argument.getCode().equals("101")),any(Semester.class));
        verify(lecturer).lecturerCourseRecord(argThat(new ArgumentMatcher<Course>() {
            @Override
            public boolean matches(Course argument) {
                return argument.getCode().equals("101");
            }
        }),any(Semester.class));
        verify(lecturer).lecturerCourseRecord(argThat(new MyCourseArgumentMatcher()),any(Semester.class));

    }
    class MyCourseArgumentMatcher implements  ArgumentMatcher<Course> {

        @Override
        public boolean matches(Course argument) {
            return argument.getCode().equals("101");
        }
    }
}
