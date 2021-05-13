package onlineexam.blakeexam.controller;

import onlineexam.blakeexam.common.QexzConst;
import onlineexam.blakeexam.dto.AjaxResult;
import onlineexam.blakeexam.entity.*;
import onlineexam.blakeexam.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/")
public class DefaultController {

    private static Log LOG = LogFactory.getLog(DefaultController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private MyClassService myClassService;

    /**
     * 首页
     */
    @RequestMapping(value="/", method= RequestMethod.GET)
    public String home(HttpServletRequest request, Model model) {
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        return "/home";
    }

    /**
     * 在线考试页面
     */
    @RequestMapping(value="/contest/index", method= RequestMethod.GET)
    public String contestIndex(HttpServletRequest request,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               Model model){
        contestService.updateStateToStart();
        contestService.updateStateToEnd();
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
//        Map<String, Object> data = contestService.getContests(page, QexzConst.contestPageSize);
        int studentId = currentAccount.getId();
        Map<String, Object> data = myClassService.getMyContestByStudentId(page, QexzConst.myClassPageSize, studentId);
        model.addAttribute(QexzConst.DATA, data);
        return "/contest/index";
    }

    /**
     *进入考试
     */
    @RequestMapping(value="/contest/{contestId}", method= RequestMethod.GET)
    public String contestDetail(HttpServletRequest request,
                                @PathVariable int contestId,
                                Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        Contest contest = contestService.getContestById(contestId);
        if( currentAccount == null || contest.getStartTime().getTime() > System.currentTimeMillis() ||
                contest.getEndTime().getTime() < System.currentTimeMillis()){
            return "redirect:/contest/index";
        }
        Paper paper = paperService.getPaperByContestId(contestId);
        List<Integer> ids = paperService.getQuestionIds(contestId);
        List<Question> questions = questionService.getQuestionsByIds(ids);
        for(Question question:questions){
            question.setAnswer("");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("paper", paper);
        data.put("contest", contest);
        data.put("questions", questions);
        model.addAttribute(QexzConst.DATA, data);
        return "/contest/detail";
    }


    /**
     * 我的课程页面
     * @param request
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/mycontest/list")
    public String myContest(HttpServletRequest request,
                            @RequestParam(value = "page", defaultValue = "1")int page, Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        int studentId = currentAccount.getId();
        Map<String, Object> data = myClassService.getMyContestByStudentId(page, QexzConst.myClassPageSize, studentId);
//        List<MyClass> myClasses = (List<MyClass>) data.get("myClasses");
//        Set<Integer> contestIds = myClasses.stream().map(MyClass::getContestId).collect(Collectors.toCollection(HashSet::new));
//        List<Contest> myContests = new ArrayList<>();
//        if(contestIds.size() > 0){
//            myContests = contestService.getContestsByContestIds(contestIds);
//            List<Subject> subjects = subjectService.getSubjects();
//            List<Account> accounts = accountService.getAccounts();
//            Map<Integer, String> subjectId2name = subjects.stream().collect(Collectors.toMap(Subject::getId, Subject::getName));
//            Map<Integer, String> accountId2name = accounts.stream().
//                    collect(Collectors.toMap(Account::getId, Account::getName));
//            for(Contest contest:myContests){
//                contest.setSubjectName(subjectId2name.getOrDefault(contest.getSubjectId(),"未知科目"));
//            }
//        }
//
//        data.put("myContests", myContests);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        model.addAttribute(QexzConst.DATA, data);
        return "/contest/myContest";
    }


    /**
     *
     * 我的课程——加入课程页面
     * @param request
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/mycontest/joincontest")
    public String enterContest(HttpServletRequest request,
                               @RequestParam(value = "page", defaultValue = "1")int page,
                               Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        int studentId = currentAccount.getId();
        Map<String, Object> data = myClassService.getNoContest(page, QexzConst.myClassPageSize, studentId);
        model.addAttribute(QexzConst.DATA, data);
        return "/contest/enterContest";
    }


    /**
     *题库中心页面
     */
    @RequestMapping(value="/problemset/list", method= RequestMethod.GET)
    public String problemSet(HttpServletRequest request,
                             @RequestParam(value = "page", defaultValue = "1") int page,Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        Map<String, Object> data = subjectService.getSubjects(page, QexzConst.subjectPageSize);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        model.addAttribute(QexzConst.DATA, data);
        return "/problem/problemset";
    }


    /**
     * 题库列表页面
     */
    @RequestMapping(value="/problemset/{problemsetId}/problems", method= RequestMethod.GET)
    public String problemList(HttpServletRequest request,
                              @RequestParam(value = "difficulty", defaultValue = "0")int difficulty,
                              @RequestParam(value = "content", defaultValue = "")String content,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @PathVariable Integer problemsetId,
                              Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        Map<String, Object> data = questionService.getQuestionsByProblemsetIdAndContentAndDiffculty(page,
                QexzConst.questionPageSize, problemsetId, content, difficulty);
        Subject subject = subjectService.getSubjectById(problemsetId);
        data.put("subject",subject);
        model.addAttribute(QexzConst.DATA, data);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        return "/problem/problemlist";
    }

    /**
     * 题库详细页面
     */
    @RequestMapping(value="/problemset/{problemsetId}/problem/{problemId}", method= RequestMethod.GET)
    public String problemDetail(HttpServletRequest request,
                                @PathVariable Integer problemsetId,
                                @PathVariable Integer problemId,
                                Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        Map<String, Object> data = new HashMap<>();
        Subject subject = subjectService.getSubjectById(problemsetId);
        Question question = questionService.getQuestionById(problemId);
        data.put("subject", subject);
        data.put("question", question);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        model.addAttribute(QexzConst.DATA, data);
        return "/problem/problemdetail";
    }



    /**
     * 获取服务器端时间,防止用户篡改客户端时间提前参与考试
     *
     * @return 时间的json数据
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult time() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new AjaxResult().setData(localDateTime);
    }

    /**
     * 测试分布式一致性session
     * @param session
     * @return
     */
    @RequestMapping(value = "/uid", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return new AjaxResult().setData(session.getId());
    }
}
