package com.kutayyaman.unittest.courserecord.application;

import com.kutayyaman.unittest.courserecord.model.Course;
import com.kutayyaman.unittest.courserecord.model.Lecturer;
import com.kutayyaman.unittest.courserecord.model.Semester;

import java.util.Optional;

public interface LecturerService {

    Optional<Lecturer> findLecturer(Course course, Semester semester);
}
