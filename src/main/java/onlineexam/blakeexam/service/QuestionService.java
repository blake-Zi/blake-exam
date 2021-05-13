package onlineexam.blakeexam.service;

import onlineexam.blakeexam.entity.Question;

import java.util.List;
import java.util.Map;

public interface QuestionService {

    int addQuestion(Question question);

    boolean updateQuestion(Question question);

    List<Question> getQuestionsByContestId(int contestId);

    //批量获取题目
    List<Question> getQuestionsByIds(List<Integer> ids);

    //通过科目id获取题目
    List<Question> getQuestionsBySubjectId(int subjectId);

    //通过contestId获取选择题
    List<Question> getCqsByContestId(int contestId);

    //通过contestId获取填空题
    List<Question> getcpsByContestId(int contestId);

    //通过contestId获取判断题
    List<Question> getjpsByContestId(int contestId);

    //通过contestId获取简答题
    List<Question> getdpsByContestId(int contestId);

    //通过contestId获取应用题
    List<Question> getapsByContestId(int contestId);

    Map<String, Object> getQuestionsByContent(int pageNum, int pageSize, String content);

    Map<String, Object> getQuestionsByProblemsetIdAndContentAndDiffculty(int pageNum, int pageSize,
                                                                         int problemsetId,
                                                                         String content,
                                                                         int diffcult);

    boolean deleteQuestion(int id);

    Question getQuestionById(int id);

    boolean updateQuestionsStateByContestId(int contestId, int state);
}
