package onlineexam.blakeexam.dao;

import onlineexam.blakeexam.entity.MyClass;
import onlineexam.blakeexam.util.MyContest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MyClassMapper {

    int insertMyclass(@Param("myClass") MyClass myClass);

    int deleteMyClass(@Param("id") int id);

    int updateMyClass(@Param("myClass") MyClass myClass);

    List<MyClass> getMyContestByContestId(@Param("contestId") int contestId);

    MyClass getMyClassById(@Param("id")int id);

    MyClass getMyClassByStudentIdAndContestId(@Param("studentId") int studentId,@Param("contestId") int contestId);

    int getMyclassCountById(@Param("studentId") int studentId);

    List<MyClass> getMyClassByStudentId(@Param("studentId") int studentId);

    List<MyClass> getMyClassByContestId(@Param("contestId") int contestId);

    int deleteByStudentId(@Param("studentId") int studentId);

    int deleteByContestId(@Param("contestId") int contestId);

}
