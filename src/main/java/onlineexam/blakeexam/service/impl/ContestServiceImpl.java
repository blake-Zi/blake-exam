package onlineexam.blakeexam.service.impl;

import com.github.pagehelper.PageHelper;
import onlineexam.blakeexam.dao.AccountMapper;
import onlineexam.blakeexam.dao.ContestMapper;
import onlineexam.blakeexam.dao.SubjectMapper;
import onlineexam.blakeexam.entity.Account;
import onlineexam.blakeexam.entity.Contest;
import onlineexam.blakeexam.entity.Subject;
import onlineexam.blakeexam.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("contestService")
public class ContestServiceImpl implements ContestService {

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private AccountMapper accountMapper;



    @Override
    public int addContest(Contest contest) {
        contest.setTotalScore(0);
        contest.setState(0);
        //结束
        if(contest.getEndTime().getTime() < System.currentTimeMillis()){
            contest.setState(2);
            //进行中
        }else if(contest.getEndTime().getTime() > System.currentTimeMillis() && contest.getStartTime().getTime() < System.currentTimeMillis()){
            contest.setState(1);
            //未开始
        }else if(contest.getStartTime().getTime() > System.currentTimeMillis()){
            contest.setState(0);
        }
        contest.setCreateTime(new Date());
        contest.setUpdateTime(new Date());
//        System.out.println(contest.toString());
        int result =  contestMapper.insertContest(contest);
//        System.out.println(contest.getId());
        return result;
    }

    @Override
    public boolean updateContest(Contest contest) {
        contest.setUpdateTime(new Date());
        if(contest.getState() != 3){
            //结束
            if(contest.getEndTime().getTime() < System.currentTimeMillis()){
                contest.setState(2);
                //进行中
            }else if(contest.getEndTime().getTime() > System.currentTimeMillis() && contest.getStartTime().getTime() < System.currentTimeMillis()){
                contest.setState(1);
                //未开始
            }else if(contest.getStartTime().getTime() > System.currentTimeMillis()){
                contest.setState(0);
            }
        }
        return contestMapper.updateContestById(contest) > 0;
    }

    @Override
    public Contest getContestById(int id) {
        return contestMapper.getContestById(id);
    }

    @Override
    public Map<String, Object> getContests(int pageNum, int pageSize) {
        Map<String,Object> data = new HashMap<>();
        int count = contestMapper.getCount();
        if(count == 0){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", 1);
            data.put("totalPageSize", 0);
            data.put("contests", new ArrayList<>());
            return data;
        }
        int totalPageNum =count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        if(pageNum > totalPageNum){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", totalPageNum);
            data.put("totalPageSize", 0);
            data.put("contests", new ArrayList<>());
            return data;
        }
        List<Subject> subjects = subjectMapper.getSubjects();
        List<Account> accounts = accountMapper.getAccounts();
        PageHelper.startPage(pageNum, pageSize);
        List<Contest> contests = contestMapper.getContests();
        //创建subjectid->name映射，accountid->name映射
        Map<Integer, String> subjectId2name = subjects.stream().
                collect(Collectors.toMap(Subject::getId, Subject::getName));
        Map<Integer, String> accountId2name = accounts.stream().
                collect(Collectors.toMap(Account::getId, Account::getName));
        for (Contest contest : contests) {
            contest.setSubjectName(subjectId2name.
                    getOrDefault(contest.getSubjectId(), "未知科目"));
            contest.setTeacherName(accountId2name.
                    getOrDefault(contest.getTeacherId(), "未知老师"));
        }
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        data.put("totalPageNum", totalPageNum);
        data.put("totalPageSize", count);
        data.put("contests", contests);
        return data;
    }

    @Override
    public Map<String, Object> getContestsByTeacherId(int pageNum, int pageSize, int teacherId) {
        Map<String,Object> data = new HashMap<>();
        int count = contestMapper.getCountByTeacherId(teacherId);
        if(count == 0){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", 1);
            data.put("totalPageSize", 0);
            data.put("contests", new ArrayList<>());
            return data;
        }
        int totalPageNum =count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        if(pageNum > totalPageNum){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", totalPageNum);
            data.put("totalPageSize", 0);
            data.put("contests", new ArrayList<>());
            return data;
        }
        List<Subject> subjects = subjectMapper.getSubjects();
        List<Account> accounts = accountMapper.getAccounts();
        PageHelper.startPage(pageNum, pageSize);
        List<Contest> contests = contestMapper.getContestsByTeacherId(teacherId);
        //创建subjectid->name映射，accountid->name映射
        Map<Integer, String> subjectId2name = subjects.stream().
                collect(Collectors.toMap(Subject::getId, Subject::getName));
        Map<Integer, String> accountId2name = accounts.stream().
                collect(Collectors.toMap(Account::getId, Account::getName));
        for (Contest contest : contests) {
            contest.setSubjectName(subjectId2name.
                    getOrDefault(contest.getSubjectId(), "未知科目"));
            contest.setTeacherName(accountId2name.
                    getOrDefault(contest.getTeacherId(), "未知老师"));
        }
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        data.put("totalPageNum", totalPageNum);
        data.put("totalPageSize", count);
        data.put("contests", contests);
        return data;
    }

    @Override
    public boolean deleteContest(int id) {
        return contestMapper.deleteContest(id) > 0;
    }

    @Override
    public boolean updateStateToStart() {
        return contestMapper.updateStateToStart(new Date()) > 0;
    }

    @Override
    public boolean updateStateToEnd() {
        return contestMapper.updateStateToEnd(new Date()) > 0;
    }

    @Override
    public List<Contest> getContestsByContestIds(Set<Integer> contestIds) {
        return contestMapper.getContestsByContestIds(contestIds);
    }

    @Override
    public int getLastId() {
        Contest contest = contestMapper.getLast();
        return contest.getId();
    }
}
