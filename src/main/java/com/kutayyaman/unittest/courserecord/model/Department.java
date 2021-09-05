package com.kutayyaman.unittest.courserecord.model;

import java.util.Set;

public class Department {
    private String code;
    private String name;
    private Set<Lecturer> lecturers;
    private Set<Student> students;
    private Faculty faculty;
}
