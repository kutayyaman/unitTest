package com.kutayyaman.unittest.courserecord;

public class StudentCourseRecord {
    private final LecturerCourseRecord lecturerCourseRecord;
    private Grade grade;
    private CourseReview courseReview;
    private Student student;

    public StudentCourseRecord(LecturerCourseRecord lecturerCourseRecord){
        this.lecturerCourseRecord = lecturerCourseRecord;
    }

    public LecturerCourseRecord getLecturerCourseRecord() {
        return lecturerCourseRecord;
    }
    public enum Grade{
        A1,A2,B1,B2,C,D,E,F
    }
}
