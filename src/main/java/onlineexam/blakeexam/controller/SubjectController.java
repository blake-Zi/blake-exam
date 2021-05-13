package onlineexam.blakeexam.controller;

import onlineexam.blakeexam.common.QexzConst;
import onlineexam.blakeexam.dto.AjaxResult;
import onlineexam.blakeexam.entity.Subject;
import onlineexam.blakeexam.service.SubjectService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {

    private static Log LOG = LogFactory.getLog(SubjectController.class);

    @Autowired
    private SubjectService subjectService;


    /**
     * 添加课程
     */
    @RequestMapping(value="/api/addSubject", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult addSubject(@RequestBody Subject subject){
        AjaxResult ajaxResult = new AjaxResult();
        subject.setImgUrl(QexzConst.DEFAULT_SUBJECT_IMG_URL);
        subject.setQuestionNum(0);
        int subjectId = subjectService.addSubject(subject);
        return new AjaxResult().setData(subjectId);
    }


    /**
     * 更新课程
     */
    @RequestMapping(value="/api/updateSubject", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateSubject(@RequestBody Subject subject){
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = subjectService.updateSubject(subject);
        return new AjaxResult().setData(result);
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/api/deleteSubject/{id}")
    public AjaxResult deleteSubject(@PathVariable int id){
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = subjectService.deleteSubjectById(id);
        return new AjaxResult().setData(result);
    }

}
