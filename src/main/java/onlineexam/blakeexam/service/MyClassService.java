package onlineexam.blakeexam.service;

import onlineexam.blakeexam.entity.MyClass;

import java.util.List;
import java.util.Map;

public interface MyClassService {

    Map<String, Object> getMyContestByStudentId (int pageNum, int pageSize, int studentId);

    List<MyClass> getMyContestByContestId (int contestId);

    Map<String, Object> getNoContest(int pageNum, int pageSize, int studentId);

    boolean addMyClass(MyClass myClass);

    boolean deleteMyClass(int id);

    boolean updateMyClass(MyClass myClass);

    MyClass getMyClassByStudentIdAndContestId(int studentId, int contestId);

    boolean deleteByMyClassStudentId(int studentId);

    boolean deleteByMyClassContestId(int contestId);
}
