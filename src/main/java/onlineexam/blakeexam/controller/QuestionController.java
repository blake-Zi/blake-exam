package onlineexam.blakeexam.controller;

import onlineexam.blakeexam.common.QexzConst;
import onlineexam.blakeexam.dto.AjaxResult;
import onlineexam.blakeexam.entity.Account;
import onlineexam.blakeexam.entity.Question;
import onlineexam.blakeexam.entity.Subject;
import onlineexam.blakeexam.service.QuestionService;
import onlineexam.blakeexam.service.SubjectService;
import onlineexam.blakeexam.util.ExcelUtil;
import onlineexam.blakeexam.util.MD5;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/question")
public class QuestionController {

    private static Log LOG = LogFactory.getLog(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SubjectService subjectService;

    /**
     * 添加题目
     */
    @RequestMapping(value="/api/addQuestion", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult addQuestion(@RequestBody Question question){
        AjaxResult ajaxResult = new AjaxResult();
        Subject subject = subjectService.getSubjectById(question.getSubjectId());
        subject.setQuestionNum(subject.getQuestionNum() + 1);
        subjectService.updateSubject(subject);
        int questionId = questionService.addQuestion(question);
        return new AjaxResult().setData(questionId);
    }

    /**
     * 批量添加题目
     * @param file
     * @param subjectId
     * @return
     */
    @RequestMapping(value = "/api/addQuestions", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addQuestions(@RequestParam("file") MultipartFile file, int subjectId){
        List<Question> questions = new ArrayList<>();
        try{
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheetAt = workbook.getSheetAt(0);
            for(Row row:sheetAt){
                //首行（即表头）不读取
                if (row.getRowNum() == 0) {
                    continue;
                }
                Question question = new Question();
                //获取标题
                String name = row.getCell(0).getStringCellValue();
                question.setTitle(name);
                //内容
                String username = row.getCell(1).getStringCellValue();
                question.setContent(username);
                //题目类型
                int questionType = (int) row.getCell(2).getNumericCellValue();
                question.setQuestionType(questionType);
                //选项ABCD
                String optiona = row.getCell(3).getStringCellValue();
                String optionb = row.getCell(4).getStringCellValue();
                String optionc = row.getCell(5).getStringCellValue();
                String optiond = row.getCell(6).getStringCellValue();
                question.setOptionA(optiona);
                question.setOptionB(optionb);
                question.setOptionC(optionc);
                question.setOptionD(optiond);
                //答案
                String answer = row.getCell(7).getStringCellValue();
                question.setAnswer(answer);
                //解析
                String parse = row.getCell(8).getStringCellValue();
                question.setParse(parse);
                //科目
                question.setSubjectId(subjectId);
                //难度
                int difficulty = (int) row.getCell(9).getNumericCellValue();
                question.setDifficulty(difficulty);
                question.setCreateTime(new Date());
                question.setUpdateTime(new Date());
                questions.add(question);
            }
            //关闭流
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        int flag = 0;
        for(Question question:questions){
            flag += questionService.addQuestion(question);
        }
        if (flag <= 0)return new AjaxResult().setMessage("添加错误！");
        return new AjaxResult().setSuccess(true);
    }

    /**
     * 导出模板
     * @param request
     * @return
     */
    @RequestMapping(value = "/export", produces = "application/octet-stream")
    public void downLoad(HttpServletRequest request, HttpServletResponse response) throws Exception{

        //excel文件名
        String fileName = "批量导入题目模板.xlsx";

        //sheet名
        String sheetName = "题目模板";

        //excel标题
        String[] title = {"标题","内容","题目类型(不是选择题不需要填选项ABCD)","选项A","选项B","选项C","选项D","答案","解析","难度(1-5)"};

        String[][] content = {{"SQL中关于视图操作，错误的说法是?","SQL中关于视图操作，错误的说法是?","0","更新视图包括插入、删除、修改三类操作","视图多用于查询","视图是实际数据库实体","安全起见， 更新视图时需要在定义视图时增加with check option子句","C","C","1"}};
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExcelUtil.getXSSFWorkbook(sheetName, title, content, null);

        //将文件存到指定位置
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return new AjaxResult().setSuccess(true);
    }

    /**
     * 更新题目
     */
    @RequestMapping(value="/api/updateQuestion", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateQuestion(@RequestBody Question question){
        AjaxResult ajaxResult = new AjaxResult();
        Question questionBefor = questionService.getQuestionById(question.getId());
        Subject subjectBefor = subjectService.getSubjectById(questionBefor.getSubjectId());
        subjectBefor.setQuestionNum(subjectBefor.getQuestionNum() - 1);
        if(subjectBefor.getQuestionNum() >= 0)
            subjectService.updateSubject(subjectBefor);
        Subject subjectAfter = subjectService.getSubjectById(question.getSubjectId());
        subjectAfter.setQuestionNum(subjectAfter.getQuestionNum() + 1);
        subjectService.updateSubject(subjectAfter);
        boolean result = questionService.updateQuestion(question);
        return new AjaxResult().setData(result);
    }

    /**
     * 删除题目
     */
    @RequestMapping("/api/deleteQuestion/{id}")
    public AjaxResult deleteQuestion(@PathVariable int id){
        AjaxResult ajaxResult = new AjaxResult();
        int subjectId = questionService.getQuestionById(id).getSubjectId();
        Subject subject = subjectService.getSubjectById(subjectId);
        subject.setQuestionNum(subject.getQuestionNum() - 1);
        if(subject.getQuestionNum() >= 0)
            subjectService.updateSubject(subject);
        boolean result = questionService.deleteQuestion(id);
        return new AjaxResult().setData(result);
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
