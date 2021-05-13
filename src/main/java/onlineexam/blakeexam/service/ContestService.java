package onlineexam.blakeexam.service;

import onlineexam.blakeexam.entity.Contest;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContestService {

    int addContest(Contest contest);

    boolean updateContest(Contest contest);

    Contest getContestById(int id);

    Map<String, Object> getContests(int pageNum, int pageSize);

    Map<String, Object> getContestsByTeacherId(int pageNum, int pageSize, int teacherId);

    boolean deleteContest(int id);

    boolean updateStateToStart();

    boolean updateStateToEnd();

    List<Contest> getContestsByContestIds(Set<Integer> contestIds);

    //获取刚创建contest的id
    int getLastId();
}
