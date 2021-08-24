package com.kutayyaman.unittest.courserecord;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

public class StudentTestWithParameterizedMethods {
    private Student student;

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class ValueSourceDemo {

        private int studentCourseSize = 0;

        @BeforeAll
        void setUp() {
            student = new Student("id1", "Ahmet", "Yilmaz");
        }

        @ParameterizedTest
        @ValueSource(strings = {"101", "103", "105"})
        void addCourseToStudent(String courseCode) { //test 3 kere caliscak ve 3unde de courseCode farkli parametre alcak 101,103,105 parametreleri icin sirasiyla bu method calistirilcak yani
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course(courseCode), new Semester());
            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            Assertions.assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class EnumSourceDemo {

        @BeforeAll
        void setUp() {
            student = new Student("id1", "Ahmet", "Yilmaz");
        }

        @ParameterizedTest
        @EnumSource(Course.CourseType.class)
        void addCourseStudent(Course.CourseType courseType) { //butun courseTypelar icin caliscak
            final Course course = Course.newCourse()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withCourseType(courseType).course();

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

        @ParameterizedTest
        @EnumSource(value = Course.CourseType.class, names = "MANDATORY")
            //sadeca MANDATORY icin caliscak
        void addMandatoryCourseToStudent(Course.CourseType courseType) {
            final Course course = Course.newCourse()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withCourseType(courseType).course();

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
            assertEquals(Course.CourseType.MANDATORY, course.getCourseType());
        }

        @ParameterizedTest
        @EnumSource(value = Course.CourseType.class, mode = EnumSource.Mode.EXCLUDE, names = "MANDATORY")
            //MANDATORY olmayanlar icin calistircak
        void addElectiveCourseToStudent(Course.CourseType courseType) {
            final Course course = Course.newCourse()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withCourseType(courseType).course();

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
            assertEquals(Course.CourseType.ELECTIVE, course.getCourseType());
        }
    }


    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class MethodSourceDemo {
        private int studentCourseSize = 0;

        @BeforeAll
        void setUp() {
            student = new Student("id1", "Ahmet", "Yilmaz");
        }

        @ParameterizedTest
        @MethodSource
        void addCourseToStudent(String courseCode) {

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course(courseCode), new Semester());
            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
        }

        Stream<String> addCourseToStudent() { //bu methodun adi yukardaki methodun adiyla ayni olmali cunku @MethodSource derken orada bir isim belirtmedik ve yukardaki String courseCode olarak 101,103,105 sirasiyla gelcek yani 3kere caliscak yukardaki method
            return Stream.of("101", "103", "105");
        }

        @ParameterizedTest
        @MethodSource("courseWithCodeAndType")
        void addCourseToStudent(String courseCode, Course.CourseType courseType) {

            final Course course = new Course(courseCode);
            course.setCourseType(courseType);
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
            assumingThat(courseCode.equals("101"),
                    () -> assertEquals(Course.CourseType.MANDATORY, courseType)
            );
            assumingThat(courseCode.equals("103"),
                    () -> assertEquals(Course.CourseType.ELECTIVE, courseType)
            );
            assumingThat(courseCode.equals("105"),
                    () -> assertEquals(Course.CourseType.MANDATORY, courseType)
            );
        }

        Stream<Arguments> courseWithCodeAndType() { //bu methodun adi yukardaki methodda @MethodSource("courseWithCodeAndType") dedigimiz icin courseWithCodeAndType bu isimle olmali
            return Stream.of(
                    Arguments.of("101", Course.CourseType.MANDATORY),
                    Arguments.of("103", Course.CourseType.ELECTIVE),
                    Arguments.of("105", Course.CourseType.MANDATORY)
            );
        }

    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class CsvSourceDemo {

        private int studentCourseSize = 0;

        @BeforeAll
        void setUp() {
            student = new Student("id1", "Ahmet", "Yilmaz");
        }

        @DisplayName("Add Course to Student")
        @ParameterizedTest(name = "{index} ==> Parameters: first:{0}, second:{1}")
        @CsvSource({"101,MANDATORY", "103, ELECTIVE", "105, MANDATORY"})
        void addCourseToStudent(String courseCode, Course.CourseType courseType) {

            final Course course = new Course(courseCode);
            course.setCourseType(courseType);
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
            assumingThat(courseCode.equals("101"),
                    () -> assertEquals(Course.CourseType.MANDATORY, courseType)
            );
            assumingThat(courseCode.equals("103"),
                    () -> assertEquals(Course.CourseType.ELECTIVE, courseType)
            );
            assumingThat(courseCode.equals("105"),
                    () -> assertEquals(Course.CourseType.MANDATORY, courseType)
            );
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/courseCodeAndTypes.csv", numLinesToSkip = 1)
        void addCourseToStudentWithCsvFile(String courseCode, Course.CourseType courseType) {

            final Course course = new Course(courseCode);
            course.setCourseType(courseType);
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
            assumingThat(courseCode.equals("101"),
                    () -> assertEquals(Course.CourseType.MANDATORY, courseType)
            );
            assumingThat(courseCode.equals("103"),
                    () -> assertEquals(Course.CourseType.ELECTIVE, courseType)
            );
            assumingThat(courseCode.equals("105"),
                    () -> assertEquals(Course.CourseType.MANDATORY, courseType)
            );
        }

    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class ArgumentsSourceDemo {
        private int studentCourseSize = 0;

        @BeforeAll
        void setUp() {
            student = new Student("id1", "Ahmet", "Yilmaz");
        }

        @ParameterizedTest
        @ArgumentsSource(CourseCodeAndTypeProvider.class)
        void addCourseToStudent(String courseCode, Course.CourseType courseType) {

            final Course course = new Course(courseCode);
            course.setCourseType(courseType);
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
            assumingThat(courseCode.equals("101"),
                    () -> assertEquals(Course.CourseType.MANDATORY, courseType)
            );
            assumingThat(courseCode.equals("103"),
                    () -> assertEquals(Course.CourseType.ELECTIVE, courseType)
            );
            assumingThat(courseCode.equals("105"),
                    () -> assertEquals(Course.CourseType.MANDATORY, courseType)
            );
        }
    }

    static class CourseCodeAndTypeProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of("101", Course.CourseType.MANDATORY),
                    Arguments.of("103", Course.CourseType.ELECTIVE),
                    Arguments.of("105", Course.CourseType.MANDATORY)

            );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class TypeConversionAndCustomDisplayNameDemo {
        // enum conversion

        @BeforeAll
        void setUp() {
            student = new Student("id1", "Ahmet", "Yilmaz");
        }

        @ParameterizedTest
        @ValueSource(strings = {"MANDATORY", "ELECTIVE"})
        void addCourseStudent(Course.CourseType courseType) {

            final Course course = Course.newCourse()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withCourseType(courseType).course();

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

        // user guide for other options


        // factory method or constructor conversion
        @ParameterizedTest
        @ValueSource(strings = {"101", "103"})
        void addCourseStudent(Course course) { //Course'in icinde tek parametre alarak calisan bir constructor oldugu icin 101 ve 103 degerleri icn 2 kere calisip 101 ve 103 parametreleriyle Course olusturcak ve oyle test yapcak
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

        // conversion using SimpleConverter with1 @ConvertWith
        @ParameterizedTest
        @ValueSource(strings = {"101", "103", "105"})
        void addCourseStudentWithConverter(@ConvertWith(CourseConverter.class) Course course) { //yukardaki 101 103 ve 105 i alip asagdaki CourseConverter ile Course olusturup buraya parametre olarak vercek
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }


        // conversion with @JavaTimeConversionPattern
        @DisplayName("Add course with localdate info")
        @ParameterizedTest(name = "{index} ==> Parametreler: {arguments}")
        @ValueSource(strings = {"01.09.2018", "01.01.2018", "01.06.2018"})
        void addCourseStudentWithLocalDate(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate localDate) {
            final Course course = Course.newCourse().withCode(String.valueOf(new Random().nextInt(100))).course();
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester(localDate));
            student.addCourse(lecturerCourseRecord);
            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }


        //display name {index}, {arguments}, {0} usage
    }

    static class CourseConverter extends SimpleArgumentConverter {

        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            return new Course(((String) source));
        }
    }
}
