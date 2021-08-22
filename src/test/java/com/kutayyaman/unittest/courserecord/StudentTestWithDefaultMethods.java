package com.kutayyaman.unittest.courserecord;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

//TestLifeCycleReporter icerisindeki @BeforeEach ve @AfterEach calisacak her testten once ve sonra
public class StudentTestWithDefaultMethods implements CreateDomain<Student>, TestLifecycleReporter {
    @Override
    public Student createDomain() { //CreateDomain interfacesinde tanimli olan createDomainShouldBeImplemented testi kendi kendine calisacak ve bu null donmedigi icin test basarili bir sekilde gecmis olacak.
        return new Student("id1", "Ahmet", "Yilmaz");
    }

    @Test
    void createStudent() {
        final Student student = createDomain();

        assertAll("Student",
                () -> assertEquals("id1", student.getId()),
                () -> assertEquals("Ahmet", student.getName()),
                () -> assertEquals("Yilmaz", student.getSurname())
        );
    }
}
