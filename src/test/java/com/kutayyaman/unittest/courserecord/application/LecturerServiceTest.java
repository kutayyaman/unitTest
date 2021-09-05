package com.kutayyaman.unittest.courserecord.application;

import com.kutayyaman.unittest.courserecord.model.Course;
import com.kutayyaman.unittest.courserecord.model.Lecturer;
import com.kutayyaman.unittest.courserecord.model.LecturerRepository;
import com.kutayyaman.unittest.courserecord.model.Semester;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

class LecturerServiceTest {

    @Test
    void findLecturer() {
        final Course course = new Course("101");
        final Semester semester = new Semester();

        final LecturerRepository lecturerRepository = Mockito.mock(LecturerRepository.class);
        final Lecturer lecturer = new Lecturer();
        Mockito.when(lecturerRepository.findByCourseAndSemester(course, semester)).thenReturn(Optional.of(lecturer));

        final LecturerService lecturerService = new LecturerServiceImpl(lecturerRepository);
        final Optional<Lecturer> lecturerOpt = lecturerService.findLecturer(course, semester);
        assertThat(lecturerOpt)
                .isPresent()
                .get()
                .isSameAs(lecturer)
        ;

        Mockito.verify(lecturerRepository).findByCourseAndSemester(course, semester); //course ve semester parametreleriyle bir kere cagirilmis mi diye kontrol ediyor
        Mockito.verify(lecturerRepository, Mockito.times(1)).findByCourseAndSemester(course, semester); // bu yukardakiyle ayni

    }
}
