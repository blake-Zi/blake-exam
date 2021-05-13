package onlineexam.blakeexam.dao;

import onlineexam.blakeexam.entity.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface GradeMapper {

    int insertGrade(@Param("grade") Grade grade);

    int deleteGrade(@Param("id") int id);

    int updateGradeById(@Param("grade") Grade grade);

    Grade getGradeById(@Param("id") int id);

    int getCountByStudentId(@Param("studentId") int studentId);

    List<Grade> getGradesByStudentId(@Param("studentId") int studentId);

    List<Grade> getGradesByContestId(@Param("contestId") int contestId);

    int deleteGradesByContestId(@Param("contestId") int contestId);

    int deleteGradesByStudentId(@Param("studentId") int studentId);
}
