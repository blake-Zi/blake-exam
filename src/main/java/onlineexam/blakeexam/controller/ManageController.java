package onlineexam.blakeexam.controller;

import onlineexam.blakeexam.common.QexzConst;
import onlineexam.blakeexam.dto.AjaxResult;
import onlineexam.blakeexam.entity.*;
import onlineexam.blakeexam.service.*;
import onlineexam.blakeexam.util.ManulPaper;
import onlineexam.blakeexam.util.PaperUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/manage")
public class ManageController {

    private static Log LOG = LogFactory.getLog(ManageController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private MyClassService myClassService;

    /**
     *管理员登陆页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        if (currentAccount == null) {
            return "/manage/manage-login";
        } else {
            return "redirect:/manage/contest/list";
        }
    }

    /**
     * 用户管理页面
     */
    @RequestMapping(value="/account/list", method= RequestMethod.GET)
    public String accountList(HttpServletRequest request,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        if(currentAccount == null || currentAccount.getLevel() < 1){
            return "/error/404";
        }else {
            Map<String, Object> data;
            if(currentAccount.getLevel() == 1 ){
                data = accountService.getAccountsByLevel(page, QexzConst.accountPageSize, 0);
            }else{
                data = accountService.getAccounts(page, QexzConst.accountPageSize);
            }
            model.addAttribute(QexzConst.DATA, data);
            return "/manage/manage-accountList";
        }
    }





    /**
     * 考试管理
     */
    @RequestMapping(value = "/contest/list", method = RequestMethod.GET)
    public String contestList(HttpServletRequest request,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        if (currentAccount == null || currentAccount.getLevel() < 1) {
            return "/error/404";
        } else {
            Map<String, Object> data;
            if (currentAccount.getLevel() == 2){
                data = contestService.getContests(page,QexzConst.contestPageSize);
            }else{
                data = contestService.getContestsByTeacherId(page,QexzConst.contestPageSize, currentAccount.getId());
            }
            List<Subject> subjects = subjectService.getSubjects();
            data.put("subjects", subjects);
            model.addAttribute(QexzConst.DATA, data);
            return "/manage/manage-contestBoard";
        }
    }

    /**
     *考试管理——查看试题
     */
    @RequestMapping(value="/contest/{contestId}/problems", method= RequestMethod.GET)
    public String contestProblemList(HttpServletRequest request,
                                     @PathVariable("contestId") Integer contestId, Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        if(currentAccount == null || currentAccount.getLevel() < 1){
            return "/error/404";
        }else{
            Map<String, Object> data = new HashMap<>();
            Contest contest = contestService.getContestById(contestId);
            Paper paper = paperService.getPaperByContestId(contestId);
            List<Integer> ids = paperService.getQuestionIds(contestId);
            List<Question> questions = questionService.getQuestionsByIds(ids);
            Integer subjectId = contest.getSubjectId();
            //用于组卷
            List<Question> questionList = questionService.getQuestionsBySubjectId(subjectId);
            List<Question> cqs = PaperUtil.sortQuestions(questionList, 0);//选择题
            List<Question> cps = PaperUtil.sortQuestions(questionList, 1);//填空题
            List<Question> dps = PaperUtil.sortQuestions(questionList, 2);//简答题
            List<Question> jps = PaperUtil.sortQuestions(questionList, 4);//判断题
            List<Question> aps = PaperUtil.sortQuestions(questionList, 3);//应用题

            data.put("cqs", cqs);
            data.put("cps", cps);
            data.put("dps", dps);
            data.put("jps", jps);
            data.put("aps", aps);

            data.put("questionsSize",questions.size());
            data.put("questions", questions);
            data.put("contest", contest);
            data.put("paper", paper);
            model.addAttribute(QexzConst.DATA, data);
            return "/manage/manage-editContestProblem";
        }

    }

    /**
     * 考试管理——查看试卷
     * @param request
     * @return
     */
    @RequestMapping(value = "/contest/{contestId}/paperDetail")
    public String paperDetail(HttpServletRequest request,
                              @PathVariable("contestId") Integer contestId, Model model
                              ){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        if(currentAccount == null || currentAccount.getLevel() < 1){
            return "/error/404";
        }else {
            Map<String, Object> data = new HashMap<>();
            Contest contest = contestService.getContestById(contestId);
            Paper paper = paperService.getPaperByContestId(contestId);
            List<Question> cqs = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getCq()));//选择题
            List<Question> cps = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getCp()));//填空题
            List<Question> dps = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getDp()));//简答题
            List<Question> jps = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getJp()));//判断题
            List<Question> aps = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getAp()));//应用题
            data.put("cqs", cqs);
            data.put("cps", cps);
            data.put("dps", dps);
            data.put("jps", jps);
            data.put("aps", aps);
            data.put("contest", contest);
            data.put("paper",paper);
            model.addAttribute(QexzConst.DATA, data);
            return "/manage/manage-editPaper";
        }
    }





    /**
     * 题目管理
     */
    @RequestMapping(value="/question/list", method= RequestMethod.GET)
    public String questionList(HttpServletRequest request,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "content", defaultValue = "") String content,
                               Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        if(currentAccount == null || currentAccount.getLevel() < 1){
            return "/error/404";
        }else{
            Map<String, Object> data = questionService.getQuestionsByContent(page,QexzConst.questionPageSize,content);
            List<Question> questions = (List<Question>) data.get("questions");
            List<Subject> subjects = subjectService.getSubjects();
            Map<Integer, String> subjectId2name = subjects.stream().
                    collect(Collectors.toMap(Subject::getId, Subject::getName));
            //System.out.println(subjectId2name);
            for(Question question: questions){
                question.setSubjectName(subjectId2name.
                        getOrDefault(question.getSubjectId(),"未知科目"));
            }
            data.put("content", content);
            data.put("subjects", subjects);
            model.addAttribute("data", data);
            return "/manage/manage-questionBoard";
        }
    }

    /**
     * 课程管理
     */
    @RequestMapping(value="/subject/list", method= RequestMethod.GET)
    public String subjectList(HttpServletRequest request,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        if(currentAccount == null || currentAccount.getLevel() < 1){
            return "/error/404";
        }else {
            Map<String, Object> data = subjectService.getSubjects(page, QexzConst.subjectPageSize);
            model.addAttribute(QexzConst.DATA, data);
            return "/manage/manage-subjectBoard";
        }
    }

    /**
     * 成绩管理——考试列表
     */
    @RequestMapping(value="/result/contest/list", method= RequestMethod.GET)
    public String resultContestList(HttpServletRequest request,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        if(currentAccount == null || currentAccount.getLevel() < 1){
            return "/error/404";
        }else {
            Map<String, Object> data;
            if (currentAccount.getLevel() == 2){
                data = contestService.getContests(page,QexzConst.contestPageSize);
            }else{
                data = contestService.getContestsByTeacherId(page,QexzConst.contestPageSize, currentAccount.getId());
            }
//            Map<String, Object> data = contestService.getContests(page, QexzConst.contestPageSize);
            List<Subject> subjects = subjectService.getSubjects();
            data.put("subjects", subjects);
            model.addAttribute(QexzConst.DATA, data);
            return "/manage/manage-resultContestBoard";
        }
    }

    /**
     * 成绩管理——考试列表——学生成绩
     */
    @RequestMapping(value="/result/contest/{contestId}/list", method= RequestMethod.GET)
    public String resultStudentList(HttpServletRequest request,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @PathVariable("contestId") int contestId,
                                    Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, currentAccount);
        if(currentAccount == null || currentAccount.getLevel() < 1){
            return "/error/404";
        }else {
            Map<String, Object> data = new HashMap<>();
            List<Grade> grades = gradeService.getGradesByContestId(contestId);
            Contest contest = contestService.getContestById(contestId);
            Paper paper = paperService.getPaperByContestId(contestId);
            //获取所有题目
            List<Integer> ids = paperService.getQuestionIds(contestId);
            List<Question> questions = questionService.getQuestionsByIds(ids);
            List<MyClass> myClasses = myClassService.getMyContestByContestId(contestId);
            //从grade集合中提取出studentId集合
//            List<Integer> studentIds = grades.stream().map(Grade::getStudentId).collect(Collectors.toList());
            List<Integer> studentIds = myClasses.stream().map(MyClass::getStudentId).collect(Collectors.toList());
            List<Account> students = accountService.getAccountsByStudentIds(studentIds);
            Map<Integer, Account> id2student = students.stream().
                    collect(Collectors.toMap(Account::getId, account -> account));
            Map<Integer, Grade> id2grade = grades.stream().
                    collect(Collectors.toMap(Grade::getStudentId, grade -> grade));
            for(MyClass myClass:myClasses){
                Account student = id2student.get(myClass.getStudentId());
                Grade grade = id2grade.get(myClass.getStudentId());
                myClass.setStudent(student);
                myClass.setGrade(grade);
            }
            data.put("myClasses", myClasses);
            data.put("grades", grades);
            data.put("contest", contest);
            data.put("questions", questions);
            model.addAttribute(QexzConst.DATA, data);
            return "/manage/manage-resultStudentBoard";
        }
    }







}
