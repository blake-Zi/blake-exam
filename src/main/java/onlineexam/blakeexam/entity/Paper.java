package onlineexam.blakeexam.entity;

public class Paper {

    //主键
    private int id;
    //试卷id
    private int contestId;
    //选择题
    private String cq;
    //填空题
    private String cp;
    //简答题
    private String dp;
    //判断题
    private String jp;
    //应用题
    private String ap;

    private int score;

    //选题题数量
    private Integer cqCount;
    //填空题数量
    private Integer cpCount;
    //判断题数量
    private Integer jpCount;
    //应用题数量
    private Integer apCount;
    //简答题数量
    private Integer dpCount;
    //答案编号
    private Integer answerid;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Paper(){

    }

    public Paper(int contestId, String cq, String cp, String jp, String dp, String ap, int score) {
        this.contestId = contestId;
        this.cq = cq;
        this.cp = cp;
        this.jp = jp;
        this.dp = dp;
        this.ap = ap;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String getCq() {
        return cq;
    }

    public void setCq(String cq) {
        this.cq = cq;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getJp() {
        return jp;
    }

    public void setJp(String jp) {
        this.jp = jp;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public Integer getCqCount() {
        return cqCount;
    }

    public void setCqCount(Integer cqCount) {
        this.cqCount = cqCount;
    }

    public Integer getCpCount() {
        return cpCount;
    }

    public void setCpCount(Integer cpCount) {
        this.cpCount = cpCount;
    }

    public Integer getJpCount() {
        return jpCount;
    }

    public void setJpCount(Integer jpCount) {
        this.jpCount = jpCount;
    }

    public Integer getApCount() {
        return apCount;
    }

    public void setApCount(Integer apCount) {
        this.apCount = apCount;
    }

    public Integer getDpCount() {
        return dpCount;
    }

    public void setDpCount(Integer dpCount) {
        this.dpCount = dpCount;
    }

    public Integer getAnswerid() {
        return answerid;
    }

    public void setAnswerid(Integer answerid) {
        this.answerid = answerid;
    }
}
