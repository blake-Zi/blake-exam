package onlineexam.blakeexam.service;

import onlineexam.blakeexam.entity.Grade;

import java.util.List;
import java.util.Map;

public interface GradeService  {

    boolean addGrade(Grade grade);

    boolean updateGrade(Grade grade);

    boolean deleteGrade(int id);

    Grade getGradeById(int id);

    Map<String, Object> getGradesByStudentId(int pageNum, int pageSize, int studentId);

    List<Grade> getGradesByContestId(int contestId);

    boolean deleteGradesByContestId(int contestId);

    boolean deleteGradesByStudentId(int studentId);
}
