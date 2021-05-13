package onlineexam.blakeexam.service;

        import onlineexam.blakeexam.dto.AjaxResult;
        import onlineexam.blakeexam.entity.Paper;
        import onlineexam.blakeexam.util.AutoPaper;
        import onlineexam.blakeexam.util.ManulPaper;

        import java.util.List;

public interface PaperService {

    boolean manualPaper(ManulPaper manulPaper);

    AjaxResult autoPaper(AutoPaper autoPaper);

    int createPaper(Integer contestId);

    Paper getPaperByContestId(Integer contestId);

    List<Integer> getQuestionIds(Integer contestId);

    boolean deleteQuestion(Integer contestId, Integer id);

}
