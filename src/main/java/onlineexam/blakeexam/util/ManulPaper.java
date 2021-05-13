package onlineexam.blakeexam.util;

import java.util.Arrays;

public class ManulPaper {

    //选择题ids
    private String[] cq;

    //填空题ids
    private String[] cp;

    //判断题ids
    private String[] jp;

    //简答题ids
    private String[] dp;

    //应用题ids
    private String[] ap;

    private int contestId;

    public ManulPaper() {


    }


    public ManulPaper(String[] cq, String[] cp, String[] jp, String[] dp, String[] ap) {
        this.cq = cq;
        this.cp = cp;
        this.jp = jp;
        this.dp = dp;
        this.ap = ap;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String[] getCq() {
        return cq;
    }

    public void setCq(String[] cq) {
        this.cq = cq;
    }

    public String[] getCp() {
        return cp;
    }

    public void setCp(String[] cp) {
        this.cp = cp;
    }

    public String[] getJp() {
        return jp;
    }

    public void setJp(String[] jp) {
        this.jp = jp;
    }

    public String[] getDp() {
        return dp;
    }

    public void setDp(String[] dp) {
        this.dp = dp;
    }

    public String[] getAp() {
        return ap;
    }

    public void setAp(String[] ap) {
        this.ap = ap;
    }

    @Override
    public String toString() {
        return "ManulPaper{" +
                "cq=" + Arrays.toString(cq) +
                ", cp=" + Arrays.toString(cp) +
                ", jp=" + Arrays.toString(jp) +
                ", dp=" + Arrays.toString(dp) +
                ", ap=" + Arrays.toString(ap) +
                '}';
    }
}
