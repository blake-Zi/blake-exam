package onlineexam.blakeexam.dao;

import onlineexam.blakeexam.entity.Contest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@Mapper
public interface ContestMapper {


    int insertContest(@Param("contest") Contest contest);

    int updateContestById(@Param("contest") Contest contest);

    Contest getContestById(@Param("id") int id);

    int getCount();

    int getCountByTeacherId(@Param("teacherId") int teacherId);

    int getCountBySubjectName(@Param("subjectName") String subjectName);

    List<Contest> getContests();

    List<Contest> getContestsByTeacherId(@Param("teacherId") int teacherId);

    int deleteContest(@Param("id") int id);

    int updateStateToStart(@Param("currentTime") Date currentTime);

    int updateStateToEnd(@Param("currentTime") Date currentTime);

    List<Contest> getContestsByContestIds(@Param("contestIds") Set<Integer> contestIds);

    Contest getLast();
}
