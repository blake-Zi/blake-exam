package onlineexam.blakeexam.util;

import onlineexam.blakeexam.entity.Contest;
import onlineexam.blakeexam.entity.Grade;

import java.util.List;

public class MyContest {
    private int studentId;
    private int contestId;
    private Grade grade;

    public MyContest() {
    }

    public MyContest(int studentId, int contestId, Grade grade) {
        this.studentId = studentId;
        this.contestId = contestId;
        this.grade = grade;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "MyContest{" +
                "studentId=" + studentId +
                ", contestId=" + contestId +
                ", grade=" + grade +
                '}';
    }
}
