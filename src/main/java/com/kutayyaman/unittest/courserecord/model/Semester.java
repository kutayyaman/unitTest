package com.kutayyaman.unittest.courserecord.model;

import java.time.LocalDate;

public class Semester {

    private final int year;
    private final Term term;
    private int addDropPeriodInWeek = 2;

    public Semester(LocalDate localDate) {
        this.year = localDate.getYear();
        this.term = term(localDate.getMonthValue());
    }

    public Semester() {
        final LocalDate now = LocalDate.now();
        this.year = now.getYear();
        this.term = term(now.getMonthValue());
    }

    private Term term(int monthValue) {

        if (monthValue >= Term.FALL.getStartMonth() || monthValue < Term.FALL.SPRING.getStartMonth()) {
            return Term.FALL;
        } else if (monthValue >= Term.SPRING.getStartMonth() && monthValue < Term.SUMMER.getStartMonth()) {
            return Term.SPRING;
        }

        return Term.SUMMER;
    }

    public boolean isActive() {
        return this.equals(new Semester());
    }


    public boolean isAddDropAllowed() {
        if (!isActive()) {
            return false;
        }

        final LocalDate endOfAddDropPeriod = LocalDate.of(this.getYear(), this.getTerm().getStartMonth(), 1).plusWeeks(addDropPeriodInWeek);

        return !LocalDate.now().isAfter(endOfAddDropPeriod);
    }

    public int getYear() {
        return year;
    }

    public Term getTerm() {
        return term;
    }

    public void setAddDropPeriodInWeek(int addDropPeriodInWeek) {
        this.addDropPeriodInWeek = addDropPeriodInWeek;
    }

    @Override
    public boolean equals(Object obj) {

        if (!Semester.class.isInstance(obj)) {
            return false;
        }

        final Semester semester = (Semester) obj;

        return semester.getYear() == this.getYear() && semester.getTerm() == this.getTerm();
    }

    @Override
    public int hashCode() {
        return 31 * Integer.parseInt(String.valueOf(getYear()) + String.valueOf(getTerm().getStartMonth()));
    }

    @Override
    public String toString() {
        return this.getTerm().name() + " of " + this.getYear();
    }
}