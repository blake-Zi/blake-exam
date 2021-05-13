package onlineexam.blakeexam.dao;

import onlineexam.blakeexam.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface QuestionMapper {

    int insertQuestion(@Param("question") Question question);

    int deleteQuestion(@Param("id") int id);

    int updateQuestionById(@Param("question") Question question);

    Question getQuestionById(@Param("id") int id);

    int getCountByContent(@Param("content") String content);

    List<Question> getQuestionsByContent(@Param("content") String content);

    //通过科目id获取题目
    List<Question> getQuestionsBySubjectId(@Param("subjectId") int subjectId);

    //通过contestId获取选择题
    List<Question> getCqsByContestId(@Param("contestId")int contestId);

    //通过contestId获取填空题
    List<Question> getcpsByContestId(@Param("contestId")int contestId);

    //通过contestId获取判断题
    List<Question> getjpsByContestId(@Param("contestId")int contestId);

    //通过contestId获取简答题
    List<Question> getdpsByContestId(@Param("contestId")int contestId);

    //通过contestId获取应用题
    List<Question> getapsByContestId(@Param("contestId")int contestId);

    List<Question> getQuestionByContestId(@Param("contestId") int contestId);

    int getCountByProblemsetIdAndContentAndDiffculty(@Param("problemsetId") int problemsetId,
                                                     @Param("content") String content,
                                                     @Param("difficulty") int diffculty);

    List<Question> getQuestionsByProblemsetIdAndContentAndDiffculty(@Param("problemsetId") int problemsetId,
                                                                    @Param("content") String content,
                                                                    @Param("difficulty") int diffculty);

    int updateQuestionsStateByContestId(@Param("contestId") int contestId,
                                        @Param("state") int state);
}
