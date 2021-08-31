package com.kutayyaman.unittest.courserecord;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class StudentTestWithAssertJAssertions {

    @Test
    void createStudent() {
        final Student student = new Student("id1", "Kutay", "Yaman");

        Assertions.assertThat(student.getName()).as("Student's name %s", student.getName())
                .doesNotContainOnlyWhitespaces()     // egerki student'in name'i "  " gibi bosluktan olusan bir string olsaydi fail olurdu test.
                .isNotEmpty() //null olmicak ama bos(yani "" boyle ) olmicak demek
                .isNotBlank() // null olamaz uzunlugu en az 1 olmali ve bu sadece bosluklardan olusamaz demek.
                .isEqualTo("Kutay")
                .isEqualToIgnoringCase("kutay") //buyuk harf kucuk harf duyarsiz
                .isIn("Kutay", "Maral")
                .isNotIn("isim1", "isim2")
                .startsWith("Ku")
                .doesNotStartWith("ku")
                .endsWith("n")
                .doesNotEndWith("a")
                .contains("uta")
                .hasSize(5);
    }


    @Test
    void addCourseToStudent() {

        final Student ahmet = new Student("id1", "Ahmet", "Yilmaz", LocalDate.of(1990, 1, 1));
        final Student mehmet = new Student("id2", "Mehmet", "Kural", LocalDate.of(1992, 1, 1));
        final Student canan = new Student("id3", "Canan", "Sahin", LocalDate.of(1995, 1, 1));
        final Student elif = new Student("id4", "Elif", "Oz", LocalDate.of(1991, 1, 1));
        final Student hasan = new Student("id5", "Hasan", "Kartal", LocalDate.of(1990, 1, 1));
        final Student mucahit = new Student("id6", "Mucahit", "Kurt", LocalDate.of(1980, 1, 1));

        final List<Student> students = new ArrayList<>(Arrays.asList(ahmet, mehmet, canan, elif, hasan));


        assertThat(students).as("Student's List")
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .contains(ahmet, mehmet)
                .contains(ahmet, Index.atIndex(0))
                .containsOnly(ahmet, mehmet, canan, hasan, elif)
                .containsExactly(ahmet, mehmet, canan, elif, hasan)
                .containsExactlyInAnyOrder(ahmet, mehmet, canan, hasan, elif)
        ;

        assertThat(students)
                .filteredOn(student -> student.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS) >= 25)
                .containsOnly(ahmet, mehmet, elif, hasan)
        ;

        assertThat(students)
                .filteredOn(new Condition() {
                    public boolean matches(Student value) {
                        return value.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS) >= 25;
                    }
                })
                .containsOnly(ahmet, mehmet, elif, hasan)
        ;

        assertThat(students)
                .filteredOn("birthDate", in(LocalDate.of(1990, 1, 1)))
                .hasSize(2)
                .containsOnly(ahmet, hasan)
        ;

        assertThat(students).as("Student's List")
                .extracting(Student::getName) //isimlerinden olusan yeni collection olusturdu
                .filteredOn(name -> name.contains("e"))
                .hasSize(2)
                .containsOnly("Ahmet", "Mehmet")
        ;

        assertThat(students)
                .filteredOn(student -> student.getName().contains("e"))
                .extracting(Student::getName, Student::getSurname)// isim ve soyisimlerden olusan yeni liste olusturdu
                .containsOnly(
                        tuple("Ahmet", "Yilmaz"),
                        tuple("Mehmet", "Kural")
                )
        ;

        final LecturerCourseRecord lecturerCourseRecord101 = new LecturerCourseRecord(new Course("101"), new Semester());
        final LecturerCourseRecord lecturerCourseRecord103 = new LecturerCourseRecord(new Course("103"), new Semester());
        final LecturerCourseRecord lecturerCourseRecord105 = new LecturerCourseRecord(new Course("105"), new Semester());

        ahmet.addCourse(lecturerCourseRecord101);
        ahmet.addCourse(lecturerCourseRecord103);

        canan.addCourse(lecturerCourseRecord101);
        canan.addCourse(lecturerCourseRecord103);
        canan.addCourse(lecturerCourseRecord105);

        assertThat(students)
                .filteredOn("name", in("Ahmet", "Canan"))
                .flatExtracting(student -> student.getStudentCourseRecords())
                .hasSize(5)
                .filteredOn(studentCourseRecord -> studentCourseRecord.getLecturerCourseRecord().getCourse().getCode().equals("101"))
                .hasSize(2)
        ;

    }

    @Test
    void anotherCreateStudentTest() {
        final Department department = new Department();
        final Student maral = new Student("id1", "Maral", "Yaman", LocalDate.of(2000, 8, 8));
        maral.setDepartment(department);
        final Student kutay = new Student("id2", "Kutay", "Yaman", LocalDate.of(1998, 6, 9));
        kutay.setDepartment(department);

        assertThat(maral).as("Check Student Maral Info")
                .isNotNull()
                .hasSameClassAs(kutay)
                .isExactlyInstanceOf(Student.class)
                .isInstanceOf(UniversityMember.class)
                .isNotEqualTo(kutay)
                .isEqualToComparingOnlyGivenFields(kutay, "surname")//maralla kutayin surname'lerinin esit olmasini bekliyor
                .isEqualToIgnoringGivenFields(kutay, "id", "name", "birthDate")
                .matches(student -> student.getBirthDate().getYear() == 2000)
                .hasFieldOrProperty("name")
                .hasNoNullFieldsOrProperties()
                .extracting(Student::getName, Student::getSurname)
                .contains("Maral", "Yaman");

        StudentAssert.assertThat(maral).as("Student maral info check")
                .hasName("Maral");

    }

    @Test
    void addCourseToStudentWithExceptionalScenarios() {

        final Student student = new Student("id1", "Maral", "Yurdakul");

        assertThatThrownBy(() -> student.addCourse(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't add course")
                .hasStackTraceContaining("Student");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> student.addCourse(null))
                .withMessageContaining("Can't add course")
                .withNoCause();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> student.addCourse(null))
                .withMessageContaining("Can't add course")
                .withNoCause();

        assertThatCode(() -> student.addCourse(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't add course");

        assertThatCode(() -> student.addCourse(new LecturerCourseRecord(new Course("101"), new Semester())))
                .doesNotThrowAnyException();

        final Throwable throwable = catchThrowable(() -> student.addCourse(null));
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't add course");
    }
}
