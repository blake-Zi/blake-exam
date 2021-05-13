package onlineexam.blakeexam.service.impl;

import com.github.pagehelper.PageHelper;
import onlineexam.blakeexam.dao.AccountMapper;
import onlineexam.blakeexam.dao.ContestMapper;
import onlineexam.blakeexam.dao.MyClassMapper;
import onlineexam.blakeexam.dao.SubjectMapper;
import onlineexam.blakeexam.entity.*;
import onlineexam.blakeexam.service.ContestService;
import onlineexam.blakeexam.service.MyClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("myclassService")
public class MyClassServiceImpl implements MyClassService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MyClassMapper myClassMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private ContestMapper contestMapper;


    /**
     * 根据studentid获取课程
     * @param pageNum
     * @param pageSize
     * @param studentId
     * @return
     */
    @Override
    public Map<String, Object> getMyContestByStudentId(int pageNum, int pageSize, int studentId) {
        Map<String, Object> data = new HashMap<>();
        int count = myClassMapper.getMyclassCountById(studentId);
        if(count == 0){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", 1);
            data.put("totalPageSize", 0);
            data.put("myClassesSize", 0);
            data.put("myClasses", new ArrayList<>());
            return data;
        }
        int totalPageNum = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        if(pageNum > totalPageNum){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", totalPageNum);
            data.put("totalPageSize", 0);
            data.put("myClassesSize", 0);
            data.put("myClasses", new ArrayList<>());
            return data;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<MyClass> myClasses = myClassMapper.getMyClassByStudentId(studentId);


        List<Subject> subjects = subjectMapper.getSubjects();
        List<Account> accounts = accountMapper.getAccounts();
        Map<Integer, String> subjectId2name = subjects.stream().
                collect(Collectors.toMap(Subject::getId, Subject::getName));
        Map<Integer, String> accountId2name = accounts.stream().
                collect(Collectors.toMap(Account::getId, Account::getName));
        for(MyClass myClass:myClasses){
            Contest contest = contestMapper.getContestById(myClass.getContestId());
            contest.setSubjectName(subjectId2name.
                    getOrDefault(contest.getSubjectId(),"未知科目"));
            contest.setTeacherName(accountId2name.
                    getOrDefault(contest.getTeacherId(),"未知老师"));
            myClass.setMyContest(contest);
//            System.out.println(myClass);
        }

        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        data.put("totalPageNum", totalPageNum);
        data.put("totalPageSize", count);
        data.put("myClassesSize", myClasses.size());
        data.put("myClasses", myClasses);
        return data;
    }

    @Override
    public List<MyClass> getMyContestByContestId(int contestId) {
        return myClassMapper.getMyContestByContestId(contestId);
    }

    /**
     * 根据studentid查找没加入的课程
     * @param pageNum
     * @param pageSize
     * @param studentId
     * @return
     */
    @Override
    public Map<String, Object> getNoContest(int pageNum, int pageSize, int studentId) {
        Map<String, Object> data = new HashMap<>();
        int mycount = myClassMapper.getMyclassCountById(studentId);

        int count = contestMapper.getCount() - mycount;
        if(count == 0){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", 1);
            data.put("totalPageSize", 0);
            data.put("myClassesSize", 0);
            data.put("myClasses", new ArrayList<>());
            return data;
        }
        int totalPageNum = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;

        if(pageNum > totalPageNum){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", totalPageNum);
            data.put("totalPageSize", 0);
            data.put("myClassesSize", 0);
            data.put("myClasses", new ArrayList<>());
            return data;
        }

        //已经加入的课程
        List<MyClass> myClasses = myClassMapper.getMyClassByStudentId(studentId);
        Set<Integer> myContestIds = myClasses.stream().map(MyClass:: getContestId).
                collect(Collectors.toCollection(HashSet::new));
        //所有课程
        PageHelper.startPage(pageNum, pageSize);
        List<Contest> contests = contestMapper.getContests();
        //没有加入的课程=所有课程 - 已经加入的课程
        for(int i = 0; i < contests.size(); i++){
            if(myContestIds.contains(contests.get(i).getId())){
                contests.remove(i);
                i--;
            }
        }
        List<Subject> subjects = subjectMapper.getSubjects();
        List<Account> accounts = accountMapper.getAccounts();
        Map<Integer, String> subjectId2name = subjects.stream().
                collect(Collectors.toMap(Subject::getId, Subject::getName));
        Map<Integer, String> accountId2name = accounts.stream().
                collect(Collectors.toMap(Account::getId, Account::getName));
        for(Contest contest:contests){
            contest.setSubjectName(subjectId2name.
                    getOrDefault(contest.getSubjectId(),"未知科目"));
            contest.setTeacherName(accountId2name.
                    getOrDefault(contest.getTeacherId(),"未知老师"));
        }

//        System.out.println();
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        data.put("totalPageNum", totalPageNum);
        data.put("totalPageSize", count);
        data.put("contestsSize", contests.size());
        data.put("contests", contests);
        return data;
    }

    @Override
    public boolean addMyClass(MyClass myClass) {
        return myClassMapper.insertMyclass(myClass) > 0;
    }

    @Override
    public boolean deleteMyClass(int id) {
        return myClassMapper.deleteMyClass(id) > 0;
    }

    @Override
    public boolean updateMyClass(MyClass myClass) {
        return myClassMapper.updateMyClass(myClass) > 0;
    }

    @Override
    public MyClass getMyClassByStudentIdAndContestId(int studentId, int contestId) {
        return myClassMapper.getMyClassByStudentIdAndContestId(studentId, contestId);
    }

    @Override
    public boolean deleteByMyClassStudentId(int studentId) {
        return myClassMapper.deleteByStudentId(studentId) > 0;
    }

    @Override
    public boolean deleteByMyClassContestId(int contestId) {
        return myClassMapper.deleteByContestId(contestId) > 0;
    }
}
