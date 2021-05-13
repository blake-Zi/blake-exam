package onlineexam.blakeexam.service.impl;

import com.github.pagehelper.PageHelper;
import onlineexam.blakeexam.dao.ContestMapper;
import onlineexam.blakeexam.dao.QuestionMapper;
import onlineexam.blakeexam.entity.Contest;
import onlineexam.blakeexam.entity.Question;
import onlineexam.blakeexam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ContestMapper contestMapper;

    @Override
    public int addQuestion(Question question) {
        if(question.getContestId() == 0){
            question.setState(1);
        }else{
            question.setState(0);
            Contest contest = contestMapper.getContestById(question.getContestId());
            contest.setTotalScore(contest.getTotalScore() + question.getScore());
            contestMapper.updateContestById(contest);
        }
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());
        return questionMapper.insertQuestion(question);
    }

    @Override
    public boolean updateQuestion(Question question) {
        if(question.getContestId() != 0){
            Contest contest = contestMapper.getContestById(question.getContestId());
            Question sourceQuestion = questionMapper.getQuestionById(question.getId());
            contest.setTotalScore(contest.getTotalScore() - sourceQuestion.getScore() + question.getScore());
            contestMapper.updateContestById(contest);
        }
        question.setUpdateTime(new Date());
        return questionMapper.updateQuestionById(question) > 0;
    }

    @Override
    public List<Question> getQuestionsByContestId(int contestId) {
        return questionMapper.getQuestionByContestId(contestId);
    }

    @Override
    public List<Question> getQuestionsByIds(List<Integer> ids) {
        List<Question> questions = new ArrayList<>();
        for(Integer id:ids){
            Question question = questionMapper.getQuestionById(id);
            questions.add(question);
        }
        return questions;
    }

    /**
     *  根据科目id获取题目
     * @param subjectId
     * @return
     */
    @Override
    public List<Question> getQuestionsBySubjectId(int subjectId) {
        return questionMapper.getQuestionsBySubjectId(subjectId);
    }


    /**
     *  获取选择题
     * @param contestId
     * @return
     */
    @Override
    public List<Question> getCqsByContestId(int contestId) {
        return questionMapper.getCqsByContestId(contestId);
    }

    /**
     * 获取填空题
     * @param contestId
     * @return
     */
    @Override
    public List<Question> getcpsByContestId(int contestId) {
        return questionMapper.getcpsByContestId(contestId);
    }

    /**
     * 获取判断题
     * @param contestId
     * @return
     */
    @Override
    public List<Question> getjpsByContestId(int contestId) {
        return questionMapper.getjpsByContestId(contestId);
    }

    /**
     * 获取简答题
     * @param contestId
     * @return
     */
    @Override
    public List<Question> getdpsByContestId(int contestId) {
        return questionMapper.getdpsByContestId(contestId);
    }

    /**
     * 获取应用题
     * @param contestId
     * @return
     */
    @Override
    public List<Question> getapsByContestId(int contestId) {
        return questionMapper.getapsByContestId(contestId);
    }

    /**
     * 根据内容查询
     * @param pageNum
     * @param pageSize
     * @param content
     * @return
     */
    @Override
    public Map<String, Object> getQuestionsByContent(int pageNum, int pageSize, String content) {
        Map<String, Object> data  = new HashMap<>();
        int count = questionMapper.getCountByContent(content);
        if(count == 0){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", 1);
            data.put("totalPageSize", 0);
            data.put("questionsSize", 0);
            data.put("questions", new ArrayList<>());
            return data;
        }
        int totalPageNum = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        if(pageNum > totalPageNum){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", totalPageNum);
            data.put("totalPageSize", 0);
            data.put("questionsSize", 0);
            data.put("questions", new ArrayList<>());
            return data;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.getQuestionsByContent(content);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        data.put("totalPageNum", totalPageNum);
        data.put("totalPageSize", count);
        data.put("questionsSize", questions.size());
        data.put("questions",questions);
        return data;
    }

    /**
     *  查询
     * @param pageNum
     * @param pageSize
     * @param problemsetId
     * @param content
     * @param diffcult
     * @return
     */
    @Override
    public Map<String, Object> getQuestionsByProblemsetIdAndContentAndDiffculty(int pageNum, int pageSize, int problemsetId, String content, int diffcult) {
        Map<String, Object> data = new HashMap<>();
        int count = questionMapper.getCountByProblemsetIdAndContentAndDiffculty(problemsetId, content, diffcult);
        if(count == 0){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", 1);
            data.put("totalPageSize", 0);
            data.put("questionsSize", 0);
            data.put("problemsetId",problemsetId);
            data.put("questions", new ArrayList<>());
            return data;
        }
        int totalPageNum = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        if(pageNum > totalPageNum){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", totalPageNum);
            data.put("totalPageSize", 0);
            data.put("questionsSize", 0);
            data.put("problemsetId",problemsetId);
            data.put("questions", new ArrayList<>());
            return data;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.getQuestionsByProblemsetIdAndContentAndDiffculty(problemsetId, content ,diffcult);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        data.put("totalPageNum", totalPageNum);
        data.put("totalPageSize", count);
        data.put("questionsSize", questions.size());
        data.put("problemsetId",problemsetId);
        data.put("questions", questions);
        return data;
    }

    @Override
    public boolean deleteQuestion(int id) {
        return questionMapper.deleteQuestion(id) > 0;
    }

    @Override
    public Question getQuestionById(int id) {
        return questionMapper.getQuestionById(id);
    }

    @Override
    public boolean updateQuestionsStateByContestId(int contestId, int state) {
        return questionMapper.updateQuestionsStateByContestId(contestId, state) > 0;
    }
}
