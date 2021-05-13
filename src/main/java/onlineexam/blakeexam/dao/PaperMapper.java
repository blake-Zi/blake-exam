package onlineexam.blakeexam.dao;

import onlineexam.blakeexam.entity.Paper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface PaperMapper {

    int addPaper(@Param("paper") Paper paper);

    int deletePaper(@Param("paperId") Integer paperId);

    int updatePaper(@Param("paper") Paper paper);

    Paper findPaperById(@Param("paperId") Integer paperId);

    Paper getPaperByContestId(@Param("contestId")Integer contestId);
}
