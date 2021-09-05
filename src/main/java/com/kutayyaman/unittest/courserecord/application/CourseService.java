package com.kutayyaman.unittest.courserecord.application;

import com.kutayyaman.unittest.courserecord.model.Course;
import com.kutayyaman.unittest.courserecord.model.Department;

import java.util.Optional;

public interface CourseService {

    Optional<Course> findCourse(Course course);

    Optional<Course> findCourse(Department department, String code, String name);
}
