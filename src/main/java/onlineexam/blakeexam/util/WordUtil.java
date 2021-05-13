package onlineexam.blakeexam.util;

import onlineexam.blakeexam.entity.Question;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.List;

public class WordUtil {

    /**
     *
     * @param title 标题
     * @param cqs 选择题
     * @param cps 填空题
     * @param dps 简答题
     * @param jps 判断题
     * @param aps 应用题
     * @param xw
     * @return
     */
    public static XWPFDocument getXWPFDocument(String title, List<Question> cqs, List<Question> cps,List<Question> dps,
                                               List<Question> jps, List<Question> aps,XWPFDocument xw){
        if (xw == null){
            xw = new XWPFDocument();
        }

        //试卷标题
        XWPFParagraph titleParagraph = xw.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);//样式居中
        XWPFRun titleFun = titleParagraph.createRun();    //创建文本对象
        titleFun.setText(title); //设置标题的名字
        titleFun.setBold(true); //加粗
        titleFun.setColor("000000");//设置颜色
        titleFun.setFontSize(28);    //字体大小


        //选择题标题
        XWPFParagraph cqtitleParagraph = xw.createParagraph();
        XWPFRun cqtitleFun = cqtitleParagraph.createRun();    //创建文本对象
        cqtitleFun.setText("一、选择题"); //设置选择题标题
        cqtitleFun.setBold(true); //加粗
        cqtitleFun.setColor("000000");//设置颜色
        cqtitleFun.setFontSize(10);    //字体大小
        for(int i = 0; i < cqs.size(); i++){
            //选择题题目
            XWPFParagraph cqsParagraph = xw.createParagraph();
            XWPFRun cqsFun = cqsParagraph.createRun();    //创建文本对象
            cqsFun.setText((i+1)+ "." + cqs.get(i).getTitle()); //设置选择题题目
            cqsFun.setColor("000000");//设置颜色
//            cqsFun.setFontFamily("宋体(中文)");
            cqsFun.setFontSize(10);    //字体大小
            //选择题选项
            createCqOption(xw,"A."+ cqs.get(i).getOptionA());
            createCqOption(xw,"B."+ cqs.get(i).getOptionB());
            createCqOption(xw,"C."+ cqs.get(i).getOptionC());
            createCqOption(xw,"D."+ cqs.get(i).getOptionD());
        }

        //填空题标题
        XWPFParagraph cptitleParagraph = xw.createParagraph();
        XWPFRun cptitleFun = cptitleParagraph.createRun();    //创建文本对象
        cptitleFun.setText("二、填空题"); //设置填空题标题
        cptitleFun.setBold(true); //加粗
        cptitleFun.setColor("000000");//设置颜色
        cptitleFun.setFontSize(10);    //字体大小
        for(int i = 0; i < cps.size(); i++){
            //填空题题目
            XWPFParagraph cpsParagraph = xw.createParagraph();
            XWPFRun cpsFun = cpsParagraph.createRun();    //创建文本对象
            cpsFun.setText((i+1)+ "." + cps.get(i).getTitle()); //设置填空题题目
            cpsFun.setColor("000000");//设置颜色
//            cpsFun.setFontFamily("宋体(中文)");
            cpsFun.setFontSize(10);    //字体大小
        }

        //判断题
        XWPFParagraph jptitleParagraph = xw.createParagraph();
        XWPFRun jptitleFun = jptitleParagraph.createRun();    //创建文本对象
        jptitleFun.setText("三、判断题"); //设置判断题标题
        jptitleFun.setBold(true); //加粗
        jptitleFun.setColor("000000");//设置颜色
        jptitleFun.setFontSize(10);    //字体大小
        for(int i = 0; i < jps.size(); i++){
            //判断题题目
            XWPFParagraph jpsParagraph = xw.createParagraph();
            XWPFRun jpsFun = jpsParagraph.createRun();    //创建文本对象
            jpsFun.setText((i + 1)+ "."  + jps.get(i).getTitle()); //设置判断题题目
            jpsFun.setColor("000000");//设置颜色
//            jpsFun.setFontFamily("宋体(中文)");
            jpsFun.setFontSize(10);    //字体大小
        }


        //简答题
        XWPFParagraph dptitleParagraph = xw.createParagraph();
        XWPFRun dptitleFun = dptitleParagraph.createRun();    //创建文本对象
        dptitleFun.setText("四、简答题"); //设置选择题标题
        dptitleFun.setBold(true); //加粗
        dptitleFun.setColor("000000");//设置颜色
        dptitleFun.setFontSize(10);    //字体大小
        for(int i = 0; i < dps.size(); i++){
            //简答题题目
            XWPFParagraph dpsParagraph = xw.createParagraph();
            XWPFRun dpsFun = dpsParagraph.createRun();    //创建文本对象
            dpsFun.setText((i+1)+ "."  + dps.get(i).getTitle()); //设置选择题题目
            dpsFun.setColor("000000");//设置颜色
//            dpsFun.setFontFamily("宋体(中文)");
            dpsFun.setFontSize(10);    //字体大小
        }

        //应用题
        XWPFParagraph aptitleParagraph = xw.createParagraph();
        XWPFRun aptitleFun = aptitleParagraph.createRun();    //创建文本对象
        aptitleFun.setText("四、应用题"); //设置选择题标题
        aptitleFun.setBold(true); //加粗
        aptitleFun.setColor("000000");//设置颜色
        aptitleFun.setFontSize(10);    //字体大小
        for(int i = 0; i < aps.size(); i++){
            //应用题题目
            XWPFParagraph apsParagraph = xw.createParagraph();
            XWPFRun apsFun = apsParagraph.createRun();    //创建文本对象
            apsFun.setText((i+1)+ "."  + aps.get(i).getTitle()); //设置选择题题目
            apsFun.setColor("000000");//设置颜色
//            apsFun.setFontFamily("宋体(中文)");
            apsFun.setFontSize(10);    //字体大小
        }

        return xw;
    }


    public static void createCqOption(XWPFDocument xw, String option){
        XWPFParagraph cqParagraph = xw.createParagraph();
        XWPFRun cqFun = cqParagraph.createRun();    //创建文本对象
        cqFun.setText(option); //设置标题的名字
        cqFun.setColor("000000");//设置颜色
//        cqFun.setFontFamily("宋体(中文)");
        cqFun.setFontSize(10);    //字体大小
    }
}
