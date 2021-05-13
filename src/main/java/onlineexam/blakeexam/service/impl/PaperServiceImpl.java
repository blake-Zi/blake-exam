package onlineexam.blakeexam.service.impl;

import onlineexam.blakeexam.dao.PaperMapper;
import onlineexam.blakeexam.dto.AjaxResult;
import onlineexam.blakeexam.entity.Paper;
import onlineexam.blakeexam.entity.Question;
import onlineexam.blakeexam.service.PaperService;
import onlineexam.blakeexam.service.QuestionService;
import onlineexam.blakeexam.util.AutoPaper;
import onlineexam.blakeexam.util.ManulPaper;
import onlineexam.blakeexam.util.PaperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("PaperService")
public class PaperServiceImpl implements PaperService {



    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private QuestionService questionService;



    @Override
    public int createPaper(Integer contestId){
        Paper paper = new Paper(contestId,"","","","","",0);
        return paperMapper.addPaper(paper);
    }

    @Override
    public Paper getPaperByContestId(Integer contestId) {
        return paperMapper.getPaperByContestId(contestId);
    }

    /**
     * 获取所有题号
     * @param contestId
     * @return
     */
    @Override
    public List<Integer> getQuestionIds(Integer contestId) {
        Paper paper = paperMapper.getPaperByContestId(contestId);
        String str = "";
        if (!paper.getCq().equals(""))
        str += paper.getCq()+ ",";
        if (!paper.getCp().equals(""))
        str += paper.getCp()+ ",";
        if (!paper.getJp().equals(""))
        str += paper.getJp()+ ",";
        if (!paper.getDp().equals(""))
        str += paper.getDp()+ ",";
        if (!paper.getAp().equals(""))
        str += paper.getAp();
        List<Integer> ids = PaperUtil.getQuestionIds(str);
        return ids;
    }

    /**
     * 通过contestid和问题id删除试卷的（id）问题
     * @param contestId
     * @param id
     * @return
     */
    @Override
    public boolean deleteQuestion(Integer contestId, Integer id) {
        Paper paper = paperMapper.getPaperByContestId(contestId);
        Question question = questionService.getQuestionById(id);
        int questionType = question.getQuestionType();
        List<Integer> cqs = PaperUtil.getQuestionIds(paper.getCq());
        List<Integer> cps = PaperUtil.getQuestionIds(paper.getCp());
        List<Integer> dps = PaperUtil.getQuestionIds(paper.getDp());
        List<Integer> jps = PaperUtil.getQuestionIds(paper.getJp());
        List<Integer> aps = PaperUtil.getQuestionIds(paper.getAp());
        //选择题
        if(questionType == 0){
            paper.setCq(PaperUtil.removeId(cqs, id));
            paper.setScore(paper.getScore() - 2);
            //填空题
        }else if(questionType == 1){
            paper.setCp(PaperUtil.removeId(cps, id));
            paper.setScore(paper.getScore() - 5);
            //简答题
        }else if(questionType == 2){
            paper.setDp(PaperUtil.removeId(dps, id));
            paper.setScore(paper.getScore() - 10);
            //判断题
        }else if(questionType == 4){
            paper.setJp(PaperUtil.removeId(jps, id));
            paper.setScore(paper.getScore() - 1);
            //应用题
        }else if(questionType == 3){
            paper.setAp(PaperUtil.removeId(aps, id));
            paper.setScore(paper.getScore() - 20);
        }

        return paperMapper.updatePaper(paper) > 0;
    }


    /**
     * 手动组卷
     * @param manulPaper
     * @return
     */
    @Override
    public boolean manualPaper(ManulPaper manulPaper) {
        int cqSize = manulPaper.getCq() == null ? 0 : manulPaper.getCq().length;
        int cpSize = manulPaper.getCp() == null ? 0 : manulPaper.getCp().length;
        int jpSize = manulPaper.getJp() == null ? 0 : manulPaper.getJp().length;
        int dpSize = manulPaper.getDp() == null ? 0 : manulPaper.getDp().length;
        int apSize = manulPaper.getAp() == null ? 0 : manulPaper.getAp().length;
//        int score = 2 * cqSize + 5 * cpSize + 1 * jpSize + 10 * dpSize + 20 * apSize;
        Paper paper = paperMapper.getPaperByContestId(manulPaper.getContestId());
        //合并
        if (cqSize != 0)
            paper.setCq(PaperUtil.mergeQestion(paper.getCq(), manulPaper.getCq()));
        if (cpSize != 0)
            paper.setCp(PaperUtil.mergeQestion(paper.getCp(), manulPaper.getCp()));
        if (jpSize != 0)
            paper.setJp(PaperUtil.mergeQestion(paper.getJp(), manulPaper.getJp()));
        if (dpSize != 0)
            paper.setDp(PaperUtil.mergeQestion(paper.getDp(), manulPaper.getDp()));
        if (apSize != 0)
            paper.setAp(PaperUtil.mergeQestion(paper.getAp(), manulPaper.getAp()));
        PaperUtil.doPaper(paper);
        //重新计算分数
        int score =  2 * paper.getCqCount() + 5 * paper.getCpCount() + 1 * paper.getJpCount() + 10 * paper.getDpCount() + 20 * paper.getApCount();

        paper.setScore(score);
        return paperMapper.updatePaper(paper) > 0;
    }

    /**
     * 自动组卷
     * @param autoPaper
     * @return
     */
    @Override
    public AjaxResult autoPaper(AutoPaper autoPaper) {
        //该map用于存放随机的各类型题号
        Map map = new HashMap<>();
        //返回记录
        boolean result = false;
        StringBuilder message = new StringBuilder();
        AjaxResult ajaxResult = new AjaxResult();
        int score = 0;
        //获取随机选择题id的集合
        List<Integer> cqids=new ArrayList<>();
        //获取该科目的所有问题
        List<Question> questions = questionService.getQuestionsBySubjectId(autoPaper.getSubjectId());
        for(Question question:questions){
            if(question.getQuestionType() == 0)
                cqids.add(question.getId());
        }
        map.put("cqids", getIds(cqids, autoPaper.getChoiceQueNumber()));
        //选择题得分
        score += 2 * autoPaper.getChoiceQueNumber();
        //获取随机填空id的集合
        List<Integer> cpids=new ArrayList<>();
        for(Question question:questions){
            if(question.getQuestionType() == 1)
                cpids.add(question.getId());
        }
        map.put("cpids", getIds(cpids, autoPaper.getCompQueNumber()));
        //填空题得分
        score += 5 * autoPaper.getCompQueNumber();
        //获取随机简答题id的集合
        List<Integer> dpids=new ArrayList<>();
        for(Question question:questions){
            if(question.getQuestionType() == 2)
                dpids.add(question.getId());
        }
        map.put("dpids", getIds(dpids, autoPaper.getDesignQueNumber()));
        //简答题得分
        score += 10 * autoPaper.getDesignQueNumber();
        //获取随机判断题id的集合
        List<Integer> jpids=new ArrayList<>();
        for(Question question:questions){
            if(question.getQuestionType() == 4)
                jpids.add(question.getId());
        }
        map.put("jpids", getIds(jpids, autoPaper.getJudgeQueNumber()));
        //判断题得分
        score += 1 * autoPaper.getJudgeQueNumber();
        //获取随机应用id的集合
        List<Integer> apids=new ArrayList<>();
        for(Question question:questions){
            if(question.getQuestionType() == 3)
                apids.add(question.getId());
        }
        map.put("apids", getIds(apids, autoPaper.getAppQueNumber()));
        //应用题得分
        score += 20 * autoPaper.getAppQueNumber();

        //添加考题
        Paper paper = paperMapper.getPaperByContestId(autoPaper.getContestId());
        String jpidfinal=(String)map.get("jpids");
        String apidfinal=(String)map.get("apids");
        String cpidfinal=(String)map.get("cpids");
        String dpidfinal=(String)map.get("dpids");
        String cqidfinal=(String)map.get("cqids");
        if(cqidfinal.equals("error")){
            message.append("选择题数量不足");
        }
        else if(cpidfinal.equals("error")){
            message.append(" 填空题数量不足");
        }
        else if(jpidfinal.equals("error")){
            message.append(" 判断题数量不足");
        }
        else if(dpidfinal.equals("error")){
            message.append(" 简答题数量不足");
        }
        else if(apidfinal.equals("error")){
            message.append(" 应用题数量不足");
        }
        else {
            //合并
            paper.setCq(cqidfinal);
            paper.setCp(cpidfinal);
            paper.setDp(dpidfinal);
            paper.setJp(jpidfinal);
            paper.setAp(apidfinal);
            paper.setScore(score);
            result = paperMapper.updatePaper(paper) > 0;
        }
        if (result)
            ajaxResult.setSuccess(result);
        else{
            ajaxResult.setMessage(message.toString());
        }

        return ajaxResult;
    }

    /**
     * 从题库中id集合抽取随机id集合
     * @param oldids
     * @param number
     * @return
     */
    public String getIds(List<Integer> oldids,int number){
        return PaperUtil.autoQustionId(oldids,number);

    }
}
