package onlineexam.blakeexam.controller;

import onlineexam.blakeexam.dto.AjaxResult;
import onlineexam.blakeexam.entity.Contest;
import onlineexam.blakeexam.entity.Paper;
import onlineexam.blakeexam.entity.Question;
import onlineexam.blakeexam.service.*;
import onlineexam.blakeexam.util.AutoPaper;
import onlineexam.blakeexam.util.ManulPaper;
import onlineexam.blakeexam.util.PaperUtil;
import onlineexam.blakeexam.util.WordUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping(value = "/contest")
public class ContestController {

    private static Log LOG = LogFactory.getLog(ContestController.class);

    @Autowired
    private PaperService paperService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private MyClassService myClassService;

    /**
     * 添加考试
     */
    @RequestMapping(value="/api/addContest", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult addContest(@RequestBody Contest contest){
        AjaxResult ajaxResult = new AjaxResult();
        int contestId = contestService.addContest(contest);
        int id = contestService.getLastId();
//        System.out.println(id);
        paperService.createPaper(id);
        return new AjaxResult().setData(contestId);
    }


    /**
     * 更新考试信息
     */
    @RequestMapping(value="/api/updateContest", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateContest(@RequestBody Contest contest){
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = contestService.updateContest(contest);
        return new AjaxResult().setData(result);
    }

    /**
     * 删除考试
     */
    @DeleteMapping("/api/deleteContest/{id}")
    public AjaxResult deleteContest(@PathVariable int id){
        AjaxResult ajaxResult = new AjaxResult();
        boolean conresult = contestService.deleteContest(id);
        //删除对应课程
        boolean mycresult = myClassService.deleteByMyClassContestId(id);
        //删除考试答题卡
        boolean graresult = gradeService.deleteGradesByContestId(id);
        return new AjaxResult().setSuccess(conresult);

    }

    /**
     *完成考试批改
     */
    @RequestMapping(value="/api/finishContest/{id}", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult finishContest(@PathVariable int id){
        AjaxResult ajaxResult = new AjaxResult();
        Contest contest = contestService.getContestById(id);
        contest.setState(3);
        questionService.updateQuestionsStateByContestId(id, 3);
        boolean result = contestService.updateContest(contest);
        return new AjaxResult().setData(result);
    }


    /**
     * 手动组卷
     * @param manulPaper
     * @return
     */
    @RequestMapping(value = "/{id}/api/manualPaper", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult manualPaper(@PathVariable("id") int id,ManulPaper manulPaper){
        manulPaper.setContestId(id);
        boolean result = paperService.manualPaper(manulPaper);
        return new AjaxResult().setSuccess(result);
    }


    /**
     * 自动组卷
     * @param autoPaper
     * @return
     */
    @RequestMapping(value = "/{id}/api/autoPaper", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult autoPaper(@PathVariable("id") int id, AutoPaper autoPaper){
        Contest contest = contestService.getContestById(id);
        autoPaper.setSubjectId(contest.getSubjectId());
        autoPaper.setContestId(id);
        AjaxResult ajaxResult = paperService.autoPaper(autoPaper);
        return ajaxResult;
    }

    /**
     * 删除试卷的id题目
     * @param contestId
     * @param id
     * @return
     */
    @RequestMapping(value = "/{contestId}/deleteProblem/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public AjaxResult deletePaperProblem(@PathVariable("contestId") int contestId,
                                         @PathVariable("id") int id){
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = paperService.deleteQuestion(contestId, id);
        return ajaxResult.setSuccess(result);
    }


    /**
     * 导出试卷
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/export/{contestId}", produces = "application/octet-stream")
    public void export(@PathVariable int contestId, HttpServletRequest request, HttpServletResponse response) throws Exception{

        Contest contest = contestService.getContestById(contestId);
        Paper paper = paperService.getPaperByContestId(contestId);
        List<Question> cqs = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getCq()));//选择题
        List<Question> cps = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getCp()));//填空题
        List<Question> dps = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getDp()));//简答题
        List<Question> jps = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getJp()));//判断题
        List<Question> aps = questionService.getQuestionsByIds(PaperUtil.getQuestionIds(paper.getAp()));//应用题

        //doc文件名
        String fileName = contest.getTitle()+ "试卷.doc";

        //tilte
        String title = contest.getTitle();

        //创建XWPFDocument
        XWPFDocument doc = WordUtil.getXWPFDocument(title, cqs, cps, dps, jps, aps, null);

        //将文件存到指定位置
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            doc.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return new AjaxResult().setSuccess(true);
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
