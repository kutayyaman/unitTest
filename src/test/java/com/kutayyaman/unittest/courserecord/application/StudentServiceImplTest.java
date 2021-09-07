package com.kutayyaman.unittest.courserecord.application;

import com.kutayyaman.unittest.courserecord.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


public class StudentServiceImplTest {

    @Mock
    private CourseService courseService;

    @Mock
    private LecturerService lecturerService;

    @Mock
    private Lecturer lecturer;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addCourse() {
        Course course = new Course("101");
        Semester semester = new Semester();
        //final CourseService courseService = mock(CourseService.class); //@Mock diyerek yukarda mockladik zaten
        when(courseService.findCourse(any())).thenReturn(Optional.of(course));

        //final LecturerService lecturerService = mock(LecturerService.class);//@Mock diyerek yukarda mockladik zaten
        //Lecturer lecturer = mock(Lecturer.class);//@Mock diyerek yukarda mockladik zaten
        when(lecturer.lecturerCourseRecord(course, semester)).thenReturn(new LecturerCourseRecord(course, semester));

        when(lecturerService.findLecturer(course, semester)).thenReturn(Optional.of(lecturer));

        //final StudentRepository studentRepository = mock(StudentRepository.class); //@Mock diyerek yukarda mockladik zaten
        Student studentKutay = new Student("id1", "Kutay", "Yaman");
        when(studentRepository.findById(anyString())).thenReturn(Optional.of(studentKutay)).thenThrow(new IllegalArgumentException("Can't find a student")).thenReturn(Optional.of(studentKutay));

        //final StudentServiceImpl studentService = new StudentServiceImpl(courseService, lecturerService, studentRepository);//@InjectMocks diyerek yukarda mockladik zaten bu aldigi parametreleri kendisi veriyor InjectMocks diyince
        studentService.addCourse("id1", course, semester);

        assertThatThrownBy(() -> studentService.findStudent("id1")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Student> studentOptional = studentService.findStudent("id1");

        assertThat(studentOptional).as("Student")
                .isPresent()
                .get()
                .matches(student -> student.isTakeCourse(course));

        verify(courseService).findCourse(course); //findCourse methodu course parametresi ile 1 defa cagirilmis olmasi gerekiyor onu kontrol ediyor burda
        verify(courseService, times(1)).findCourse(course);//buda yukardakiyle ayni sadece kac kere cagrildigini parametre olarak verdik eger vermezsek default olarak 1 yapiyor zaten
        verify(courseService, atLeast(1)).findCourse(course);//en az 1 defa cagrilmis diye kontrol ettik
        verify(courseService, atMost(1)).findCourse(course); // en fazla 1 kere cagrilmis diye kontrol ettik

        verify(studentRepository, times(3)).findById("id1");

        verify(lecturerService).findLecturer(any(Course.class), any(Semester.class));

        verify(lecturer).lecturerCourseRecord(argThat(argument -> argument.getCode().equals("101")), any(Semester.class));
        verify(lecturer).lecturerCourseRecord(argThat(new ArgumentMatcher<Course>() {
            @Override
            public boolean matches(Course argument) {
                return argument.getCode().equals("101");
            }
        }), any(Semester.class));
        verify(lecturer).lecturerCourseRecord(argThat(new MyCourseArgumentMatcher()), any(Semester.class));

    }

    @Test
    void dropCourse() {

        final Course course = new Course("101");

        when(courseService.findCourse(course))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(course));
        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
        when(lecturer.lecturerCourseRecord(eq(course), any(Semester.class))).thenReturn(lecturerCourseRecord);
        when(lecturerService.findLecturer(eq(course), any(Semester.class)))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(lecturer));
        final Student student = mock(Student.class);
        when(studentRepository.findById("id1"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(student));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.dropCourse("id1", course))
                .withMessageContaining("Can't find a student");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.dropCourse("id1", course))
                .withMessageContaining("Can't find a course");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.dropCourse("id1", course))
                .withMessageContaining("Can't find a lecturer");

        studentService.dropCourse("id1", course);

        InOrder inOrder = inOrder(studentRepository, courseService, lecturerService, lecturer, student); // bunlarin calismasini sirayla kontrol ediyoruz

        inOrder.verify(studentRepository, times(2)).findById("id1");
        inOrder.verify(courseService, times(1)).findCourse(course);

        inOrder.verify(studentRepository, times(1)).findById("id1");
        inOrder.verify(courseService, times(1)).findCourse(course);
        inOrder.verify(lecturerService, times(1)).findLecturer(eq(course), any(Semester.class));

        inOrder.verify(studentRepository, times(1)).findById("id1");
        inOrder.verify(courseService, times(1)).findCourse(course);
        inOrder.verify(lecturerService, times(1)).findLecturer(eq(course), any(Semester.class));
        inOrder.verify(lecturer).lecturerCourseRecord(argThat(argument -> argument.getCode().equals("101")), any(Semester.class));
        inOrder.verify(student).dropCourse(lecturerCourseRecord);
        inOrder.verify(studentRepository).save(student);

        verifyNoMoreInteractions(studentRepository, courseService, lecturerService, lecturer, student); //bu parametre olarak verdigim mocklar ile alakali benim verify ettiklerim disinda bir seyler varsa bana hata donecek
        verifyZeroInteractions(studentRepository, courseService, lecturerService, lecturer, student); //bu parametre olarak verdigim mocklar ile alakali benim verify ettiklerim disinda bir seyler varsa bana hata donecek
    }

    @Test
    void deleteStudent() {
        final Student student = new Student("id1", "Kutay", "Yaman");
        when(studentRepository.findById("id1")).thenAnswer(invocation -> Optional.of(student));

        doNothing()//ilk cagirmada bir sey yapma
                .doThrow(new IllegalArgumentException("There is no student in repo"))//ikincide exception firlat
                .doAnswer(invocation -> {//3.de methodun icindekini yap
                    final Student studentKutay = invocation.getArgument(0); //studentRepository'nin delete methodunun ilk parametresini aliyoruz oda zaten student'di ki tek parametre aliyordu zaten StudentServiceImpl'de gorebiliriz.
                    System.out.println(String.format("Student<%s> will be deleted!", studentKutay));
                    return null;
                })
                .when(studentRepository).delete(student); //studentRepository'nin delete'i void oldugu icin bu sekilde yapiliyor

        studentService.deleteStudent("id1"); //ilk cagirilmasinda bir sey yapmicak
        assertThatIllegalArgumentException().isThrownBy(() -> studentService.deleteStudent("id1"))
                .withMessageContaining("no student");
        studentService.deleteStudent("id1"); //bu 3. defa cagirildiginda yukardaki doAnswer icindeki method cagirilcak yani ogrenci bilgileri yazdirilacak
        studentService.deleteStudent("id1"); //bu 4. defa cagirildiginda yine zaten 3 tane cagirilmaya gore yaptigimiz icin 3. cagirilmada ne oluyorsa o olcak yani ekrana yazcak yine.

        verify(studentRepository, times(4)).findById(stringArgumentCaptor.capture());
        verify(studentRepository, times(4)).delete(studentArgumentCaptor.capture());

        assertThat(stringArgumentCaptor.getAllValues())
                .hasSize(4)
                .containsOnly("id1", "id1", "id1");

        assertThat(studentArgumentCaptor.getAllValues())
                .hasSize(4)
                .extracting(Student::getId)
                .containsOnly("id1", "id1", "id1");
    }

    @Test
    void addCourseWithSpyStudent() {

        final Course course = new Course("101");
        final Semester semester = new Semester();
        when(courseService.findCourse(any())).thenReturn(Optional.of(course));
        when(lecturer.lecturerCourseRecord(course, semester)).thenReturn(new LecturerCourseRecord(course, semester));
        when(lecturerService.findLecturer(course, semester)).thenReturn(Optional.of(lecturer));
        final Student studentReal = new Student("id1", "Ahmet", "Yilmaz");
        final Student studentAhmet = spy(studentReal);

//        doThrow(new IllegalArgumentException("Spy failed!")).when(studentAhmet).addCourse(any(LecturerCourseRecord.class));

        doReturn(BigDecimal.ONE).when(studentAhmet).gpa();

        when(studentRepository.findById(anyString()))
                .thenReturn(Optional.of(studentAhmet)).
                thenThrow(new IllegalArgumentException("Can't find a student"))
                .thenReturn(Optional.of(studentAhmet));

        studentService.addCourse("id1", course, semester);

        assertThat(studentAhmet)
                .matches(student -> student.isTakeCourse(course));

        assertThat(studentReal)
                .matches(student -> student.isTakeCourse(course));

        studentAhmet.setBirthDate(LocalDate.of(1990, 1, 1));
        assertThat(studentAhmet.getBirthDate()).isNotNull();
        assertThat(studentReal.getBirthDate()).isNull();

        assertThatThrownBy(() -> studentService.findStudent("id1")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Student> studentOptional = studentService.findStudent("id1");

        assertThat(studentOptional).as("Student")
                .isPresent()
                .get()
                .matches(student -> student.isTakeCourse(course))
        ;

        verify(courseService).findCourse(course);
        verify(courseService, times(1)).findCourse(course);
        verify(courseService, atLeast(1)).findCourse(course);
        verify(courseService, atMost(1)).findCourse(course);

        verify(studentRepository, times(3)).findById("id1");

        verify(lecturerService).findLecturer(any(Course.class), any(Semester.class));

        verify(lecturer).lecturerCourseRecord(argThat(argument -> argument.getCode().equals("101")), any(Semester.class));
        verify(lecturer).lecturerCourseRecord(argThat(new MyCourseArgumentMatcher()), any(Semester.class));
    }

    @Test
    void addCourseWithPartialMock() {

        final Course course = new Course("101");
        final Semester semester = new Semester();
        when(courseService.findCourse(any())).thenReturn(Optional.of(course));
        when(lecturer.lecturerCourseRecord(course, semester)).thenReturn(new LecturerCourseRecord(course, semester));
        when(lecturerService.findLecturer(course, semester)).thenReturn(Optional.of(lecturer));
        final Student studentAhmet = mock(Student.class);
        when(studentAhmet.isTakeCourse(any(Course.class))).thenReturn(true);
        when(studentRepository.findById(anyString()))
                .thenReturn(Optional.of(studentAhmet)).
                thenThrow(new IllegalArgumentException("Can't find a student"))
                .thenReturn(Optional.of(studentAhmet));

        assertThat(studentAhmet.gpa()).isNull();
        doCallRealMethod().when(studentAhmet).gpa();
//        when().thenCallRealMethod();
        assertThatNullPointerException().isThrownBy(studentAhmet::gpa);

        studentService.addCourse("id1", course, semester);

        assertThatThrownBy(() -> studentService.findStudent("id1")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Student> studentOptional = studentService.findStudent("id1");

        assertThat(studentOptional).as("Student")
                .isPresent()
                .get()
                .matches(student -> student.isTakeCourse(course))
        ;

        verify(courseService).findCourse(course);
        verify(courseService, times(1)).findCourse(course);
        verify(courseService, atLeast(1)).findCourse(course);
        verify(courseService, atMost(1)).findCourse(course);

        verify(studentRepository, times(3)).findById("id1");

        verify(lecturerService).findLecturer(any(Course.class), any(Semester.class));

        verify(lecturer).lecturerCourseRecord(argThat(argument -> argument.getCode().equals("101")), any(Semester.class));
        verify(lecturer).lecturerCourseRecord(argThat(new MyCourseArgumentMatcher()), any(Semester.class));

    }

    @Test
    void addCourseWithBDD() { // BDD: Behavior Driven Development

        final Course course = new Course("101");
        final Semester semester = new Semester();
        given(courseService.findCourse(any())).willReturn(Optional.of(course));
        given(lecturer.lecturerCourseRecord(course, semester)).willReturn(new LecturerCourseRecord(course, semester));
        given(lecturerService.findLecturer(course, semester)).willReturn(Optional.of(lecturer));
        final Student studentAhmet = new Student("id1", "Ahmet", "Yilmaz");
        given(studentRepository.findById(anyString()))
                .willReturn(Optional.of(studentAhmet)).
                willThrow(new IllegalArgumentException("Can't find a student"))
                .willReturn(Optional.of(studentAhmet));

        studentService.addCourse("id1", course, semester);

        assertThatThrownBy(() -> studentService.findStudent("id1")).isInstanceOf(IllegalArgumentException.class);

        final Optional<Student> studentOptional = studentService.findStudent("id1");

        assertThat(studentOptional).as("Student")
                .isPresent()
                .get()
                .matches(student -> student.isTakeCourse(course))
        ;

        then(courseService).should().findCourse(course);
        then(courseService).should(times(1)).findCourse(course);
        then(courseService).should(atLeast(1)).findCourse(course);
        then(courseService).should(atMost(1)).findCourse(course);

        then(studentRepository).should(times(3)).findById("id1");

        then(lecturerService).should().findLecturer(any(Course.class), any(Semester.class));

        then(lecturer).should().lecturerCourseRecord(argThat(argument -> argument.getCode().equals("101")), any(Semester.class));
        then(lecturer).should().lecturerCourseRecord(argThat(new MyCourseArgumentMatcher()), any(Semester.class));
    }

    class MyCourseArgumentMatcher implements ArgumentMatcher<Course> {

        @Override
        public boolean matches(Course argument) {
            return argument.getCode().equals("101");
        }
    }
}
