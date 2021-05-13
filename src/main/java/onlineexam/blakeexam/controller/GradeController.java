package onlineexam.blakeexam.controller;


import onlineexam.blakeexam.common.QexzConst;
import onlineexam.blakeexam.dto.AjaxResult;
import onlineexam.blakeexam.entity.Account;
import onlineexam.blakeexam.entity.Grade;
import onlineexam.blakeexam.entity.MyClass;
import onlineexam.blakeexam.entity.Question;
import onlineexam.blakeexam.service.GradeService;
import onlineexam.blakeexam.service.MyClassService;
import onlineexam.blakeexam.service.PaperService;
import onlineexam.blakeexam.service.QuestionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/grade")
public class GradeController {

    private static Log LOG = LogFactory.getLog(GradeController.class);

    @Autowired
    private GradeService gradeService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private MyClassService myClassService;


    /**
     *提交试卷
     */
    @RequestMapping(value="/api/submitContest", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult submitContest(HttpServletRequest request,
                                    @RequestBody Grade grade){
        AjaxResult ajaxResult = new AjaxResult();
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        List<String> answerStrs = Arrays.asList(grade.getAnswerJson().split(QexzConst.SPLIT_CHAR));
        int autoResult = 0;
        List<Integer> ids = paperService.getQuestionIds(grade.getContestId());
        List<Question> questions = questionService.getQuestionsByIds(ids);
        for(int i = 0; i < questions.size(); i++){
            Question question = questions.get(i);
            //修改选择题
            if(question.getQuestionType() == 0  && question.getAnswer().equals(answerStrs.get(i))){
                autoResult += 2;
            }
            //修改填空题
            if(question.getQuestionType() == 1){
                //题目答案
                List<String> QueAns = Arrays.asList(question.getAnswer().split(";"));
//                System.out.println(QueAns.toString());
                //用户答案
                List<String> userAns =Arrays.asList(answerStrs.get(i).split(";"));
//                System.out.println(userAns.toString());
                boolean flag = true;
                for(int j = 0; j < QueAns.size(); j++){
                    if (!userAns.contains(QueAns.get(j))) {
                        flag = false;
                        break;
                    }
//                    System.out.println(j+":"+flag);
                }
                if (flag)
                autoResult += 5;
            }
            //修改判断题
            if( question.getQuestionType() == 4 && question.getAnswer().equals(answerStrs.get(i))){
                autoResult += 1;
            }
        }
        grade.setStudentId(currentAccount.getId());
        grade.setResult(autoResult);
        grade.setAutoResult(autoResult);
        grade.setManulResult(0);
        boolean gradeResult =gradeService.addGrade(grade);
        MyClass myClass = myClassService.getMyClassByStudentIdAndContestId(currentAccount.getId(), grade.getContestId());
        myClass.setState(1);
        myClass.setFinishTime(new Date());
        boolean myResult = myClassService.updateMyClass(myClass);
        return new AjaxResult().setSuccess(gradeResult && myResult);
    }

    /**
     *完成批改试卷
     */

    @RequestMapping(value="/api/finishGrade", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult finishGrade(@RequestBody Grade grade){
        AjaxResult ajaxResult = new AjaxResult();
        grade.setResult(grade.getAutoResult() + grade.getManulResult());
        grade.setFinishTime(new Date());
        grade.setState(1);
        boolean result = gradeService.updateGrade(grade);
        return new AjaxResult().setData(result);
    }

}
