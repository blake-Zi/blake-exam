package onlineexam.blakeexam.controller;

import onlineexam.blakeexam.dto.AjaxResult;
import onlineexam.blakeexam.entity.MyClass;
import onlineexam.blakeexam.service.MyClassService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/mycontest")
public class MyClassController {

    private static Log LOG = LogFactory.getLog(MyClassController.class);

    @Autowired
    private MyClassService myClassService;


    @RequestMapping(value = "/api/join", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertContest(@RequestBody MyClass myClass){
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = myClassService.addMyClass(myClass);
        return ajaxResult.setSuccess(result);
    }


    @RequestMapping(value = "/api/out/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public AjaxResult deleteMyContest(@PathVariable("id") int id){
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = myClassService.deleteMyClass(id);
        return ajaxResult.setSuccess(result);
    }
}
