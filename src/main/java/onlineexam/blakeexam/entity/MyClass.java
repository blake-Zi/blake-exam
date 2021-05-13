package onlineexam.blakeexam.entity;

import java.util.Date;
import java.util.List;

public class MyClass {

    private int id;
    private int studentId;
    private int contestId;
    private Date createTime;
    private Date finishTime;


    private Account student;
    private Contest myContest;
    private Grade grade;

    //记录考生是否已经交卷
    private int state;

    public MyClass() {
    }

    public MyClass(int studentId, int contestId, Date createTime, Date finishTime, int state) {
        this.studentId = studentId;
        this.contestId = contestId;
        this.createTime = createTime;
        this.finishTime = finishTime;
        this.state = state;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Account getStudent() {
        return student;
    }

    public void setStudent(Account student) {
        this.student = student;
    }

    public Contest getMyContest() {
        return myContest;
    }

    public void setMyContest(Contest myContest) {
        this.myContest = myContest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MyClass{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", contestId=" + contestId +
                ", createTime=" + createTime +
                ", finishTime=" + finishTime +
                ", student=" + student +
                ", myContest=" + myContest +
                ", grade=" + grade +
                ", state=" + state +
                '}';
    }
}
