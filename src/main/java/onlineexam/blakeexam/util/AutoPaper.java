package onlineexam.blakeexam.util;

public class AutoPaper {
    /**选择题个数*/
    private Integer ChoiceQueNumber;

    /**填空题个数*/
    private Integer CompQueNumber;

    /**简答题个数*/
    private Integer DesignQueNumber;

    /**判断题个数*/
    private Integer JudgeQueNumber;

    /**应用题个数*/
    private Integer AppQueNumber;


    //科目id
    private int subjectId;

    //试卷id
    private int contestId;

    public AutoPaper() {
    }

    public AutoPaper(Integer choiceQueNumber, Integer compQueNumber, Integer designQueNumber, Integer judgeQueNumber, Integer appQueNumber, int subjectId, int contestId) {
        ChoiceQueNumber = choiceQueNumber;
        CompQueNumber = compQueNumber;
        DesignQueNumber = designQueNumber;
        JudgeQueNumber = judgeQueNumber;
        AppQueNumber = appQueNumber;
        this.subjectId = subjectId;
        this.contestId = contestId;
    }

    public Integer getChoiceQueNumber() {
        return ChoiceQueNumber;
    }

    public void setChoiceQueNumber(Integer choiceQueNumber) {
        ChoiceQueNumber = choiceQueNumber;
    }

    public Integer getCompQueNumber() {
        return CompQueNumber;
    }

    public void setCompQueNumber(Integer compQueNumber) {
        CompQueNumber = compQueNumber;
    }

    public Integer getDesignQueNumber() {
        return DesignQueNumber;
    }

    public void setDesignQueNumber(Integer designQueNumber) {
        DesignQueNumber = designQueNumber;
    }

    public Integer getJudgeQueNumber() {
        return JudgeQueNumber;
    }

    public void setJudgeQueNumber(Integer judgeQueNumber) {
        JudgeQueNumber = judgeQueNumber;
    }

    public Integer getAppQueNumber() {
        return AppQueNumber;
    }

    public void setAppQueNumber(Integer appQueNumber) {
        AppQueNumber = appQueNumber;
    }


    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    @Override
    public String toString() {
        return "AutoPaper{" +
                "ChoiceQueNumber=" + ChoiceQueNumber +
                ", CompQueNumber=" + CompQueNumber +
                ", DesignQueNumber=" + DesignQueNumber +
                ", JudgeQueNumber=" + JudgeQueNumber +
                ", AppQueNumber=" + AppQueNumber +
                ", subjectId=" + subjectId +
                ", contestId=" + contestId +
                '}';
    }
}
