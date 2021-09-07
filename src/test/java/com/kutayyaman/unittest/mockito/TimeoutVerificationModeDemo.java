package com.kutayyaman.unittest.mockito;

import com.kutayyaman.unittest.courserecord.model.Student;
import com.kutayyaman.unittest.courserecord.model.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class TimeoutVerificationModeDemo {

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void readStudent() {
        when(studentRepository.findById("id1")).thenReturn(Optional.of(new Student("","","")));

        new Thread(() -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            studentRepository.findById("id1");
        }).start();

        verify(studentRepository,timeout(100).times(1)).findById("id1"); // 1 milisaniye bekleyip sonra yapiyor bu aslinda threadlerle falan calisirken anlamli oluyor
    }
}
