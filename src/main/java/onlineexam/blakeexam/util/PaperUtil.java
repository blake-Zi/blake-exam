package onlineexam.blakeexam.util;

import onlineexam.blakeexam.entity.Paper;
import onlineexam.blakeexam.entity.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 试卷工具类
 */
public class PaperUtil {

    /**
     * 计算每种题型一共有多少，并赋值
     * @param paper
     */
    public static void doPaper(Paper paper){
            paper.setCpCount(paper.getCp() == "" ? 0 : paper.getCp().split(",").length);
            paper.setApCount(paper.getAp() == "" ? 0 : paper.getAp().split(",").length);
            paper.setCqCount(paper.getCq() == "" ? 0 : paper.getCq().split(",").length);
            paper.setDpCount(paper.getDp() == "" ? 0 : paper.getDp().split(",").length);
            paper.setJpCount(paper.getJp() == "" ? 0 : paper.getJp().split(",").length);
    }

    /**
     * 获取题号
     * @param str
     * @return
     */
    public static List<Integer> getQuestionIds(String str){
        List<Integer> _ids=new ArrayList<>();
        if (str.equals(""))return _ids;
        if(!str.equals("error")){
            String[] ids=str.split(",");
            for(String id:ids){
                Integer _id=Integer.parseInt(id);
                _ids.add(_id);
            }
        }

        return _ids;
    }


    /**
     * 获取随机试题题号
     * @param ids 数据库获取的题号字符串
     * @param number 题数
     * @return  随机试题集合
     */
    public static String autoQustionId(List<Integer> ids,Integer number){
        String message="";
        if(ids.size()<number){
            message="error";
        }
        else{
            Map map = new HashMap();
            String str="";
            // List listNew = new ArrayList();
            while (map.size() < number) {
                int random = (int) (Math.random() * ids.size());
                if (!map.containsKey(random)) {
                    map.put(random, "");
                    str+=ids.get(random)+",";
                }
            }
            message=str.substring(0,str.length()-1);
        }
        return message;
    }


    /**
     * 数组转字符串（，隔开）
     * @param message
     * @return
     */
    public static String array2String (String[] message){
        if(message == null) return "";
        String ids="";
        for(String str:message){
            ids+=str+",";
        }
        ids=ids.substring(0,ids.length()-1);
        return ids;
    }

    /**
     * 根据题目id（区分单选简答之类）筛选
     * @param questions
     * @param questionType
     * @return
     */
    public static List<Question> sortQuestions(List<Question> questions, int questionType){
        List<Question> list = new ArrayList<>();
        for(Question question: questions){
            if(question.getQuestionType() == questionType){
                list.add(question);
            }
        }
        return list;
    }

    /**
     * 合并问题id（防止重复）
     * @param before
     * @param add
     * @return
     */
    public static String mergeQestion(String before , String[] add){
        List<Integer> after = getQuestionIds(before);
        for(String s:add){
            Integer id = Integer.parseInt(s);
            if(!after.contains(id)){
                after.add(id);
            }
        }
        String ids = "";
        for(int i = 0; i < after.size(); i++ ){
            ids +=after.get(i)+",";
        }
        ids = ids.substring(0,ids.length()-1);
        return ids;
    }

    /**
     * 移除list指定id，并返回string
     * @param list
     * @param id
     * @return
     */
    public static String removeId(List<Integer> list, int id){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) == id){
                list.remove(i);
                break;
            }
        }
        String ids = "";
        for(int i = 0; i < list.size(); i++ ){
            ids +=list.get(i)+",";
        }
        if(ids.equals(""))return ids;
        ids = ids.substring(0,ids.length()-1);
        return ids;
    }
}
