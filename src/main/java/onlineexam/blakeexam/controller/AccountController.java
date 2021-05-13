package onlineexam.blakeexam.controller;

import onlineexam.blakeexam.common.QexzConst;
import onlineexam.blakeexam.entity.Account;
import onlineexam.blakeexam.dto.AjaxResult;
import onlineexam.blakeexam.entity.Contest;
import onlineexam.blakeexam.entity.Grade;
import onlineexam.blakeexam.entity.Subject;
import onlineexam.blakeexam.exception.QexzWebError;
import onlineexam.blakeexam.service.*;
import onlineexam.blakeexam.util.ExcelUtil;
import onlineexam.blakeexam.util.MD5;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

    private static Log LOG = LogFactory.getLog(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private MyClassService myClassService;

    /**
     * 个人信息页面
     */
    @RequestMapping(value="/profile", method= RequestMethod.GET)
    public String profile(HttpServletRequest request, Model model) {
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        //TODO::拦截器过滤处理
        if (currentAccount == null) {
            //用户未登录直接返回首页面
            return "redirect:/";
        }
        Account current_account = accountService.getAccountByUsername(currentAccount.getUsername());
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, current_account);
        return "/user/profile";
    }

    /**
     * 更改密码页面
     */
    @RequestMapping(value="/password", method= RequestMethod.GET)
    public String password(HttpServletRequest request, Model model) {
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        //TODO::拦截器过滤处理
        if (currentAccount == null) {
            //用户未登录直接返回首页面
            return "redirect:/";
        }
        Account current_account = accountService.getAccountByUsername(currentAccount.getUsername());
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, current_account);
        return "/user/password";
    }


    /**
     * 更新密码
     */
    @RequestMapping(value = "/api/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updatePassword(HttpServletRequest request, HttpServletResponse response) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");
            String md5OldPassword = MD5.md5(QexzConst.MD5_SALT+oldPassword);
            String md5NewPassword = MD5.md5(QexzConst.MD5_SALT+newPassword);
            if (StringUtils.isNotEmpty(newPassword) && StringUtils.isNotEmpty(confirmNewPassword)
                    && !newPassword.equals(confirmNewPassword)) {
                return AjaxResult.fixedError(QexzWebError.NOT_EQUALS_CONFIRM_PASSWORD);
            }
            Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
            if (!currentAccount.getPassword().equals(md5OldPassword)) {
                return AjaxResult.fixedError(QexzWebError.WRONG_PASSWORD);
            }
            currentAccount.setPassword(md5NewPassword);
            boolean result = accountService.updateAccount(currentAccount);
            ajaxResult.setSuccess(result);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return AjaxResult.fixedError(QexzWebError.COMMON);
        }
        return ajaxResult;
    }

    /**
     * 更新个人信息
     */
    @RequestMapping(value = "/api/updateAccount", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateAccount(HttpServletRequest request, HttpServletResponse response) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            String phone = request.getParameter("phone");
            String qq = request.getParameter("qq");
            String email = request.getParameter("email");
            String description = request.getParameter("description");
            String avatarImgUrl = request.getParameter("avatarImgUrl");

            Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
            currentAccount.setPhone(phone);
            currentAccount.setQq(qq);
            currentAccount.setEmail(email);
            currentAccount.setDescription(description);
            currentAccount.setAvatarImgUrl(avatarImgUrl);
            boolean result = accountService.updateAccount(currentAccount);
            ajaxResult.setSuccess(result);

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return AjaxResult.fixedError(QexzWebError.COMMON);
        }

        return ajaxResult;
    }

    /**
     *考试记录页面
     */
    @RequestMapping(value="/myExam", method= RequestMethod.GET)
    public String myExam(HttpServletRequest request,
                         @RequestParam(value = "page", defaultValue = "1") int page,
                         Model model){
        Account currentAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
        if(currentAccount == null){
            return "redirect:/";
        }
        Map<String, Object> data = gradeService.getGradesByStudentId(page, QexzConst.gradePageSize,currentAccount.getId());
        List<Grade> grades = (List<Grade>) data.get("grades");
        if (grades != null){
            Set<Integer> contestIds = grades.stream().map(Grade::getContestId).collect(Collectors.toCollection(HashSet::new));
            List<Contest> contests = contestService.getContestsByContestIds(contestIds);
            List<Subject> subjects = subjectService.getSubjects();
            Map<Integer, String> subjectId2name = subjects.stream().collect(Collectors.toMap(Subject::getId, Subject::getName));
            for(Contest contest:contests){
                contest.setSubjectName(subjectId2name.getOrDefault(contest.getSubjectId(),"未知科目"));
            }
            Map<Integer, Contest> id2contest = contests.stream().collect(Collectors.toMap(Contest::getId, contest -> contest));
            for(Grade grade:grades){
                grade.setContest(id2contest.get(grade.getContestId()));
            }
        }
        //同步数据库
        Account current_account = accountService.getAccountByUsername(currentAccount.getUsername());

        model.addAttribute(QexzConst.DATA, data);
        model.addAttribute(QexzConst.CURRENT_ACCOUNT, current_account);
        return "/user/myExam";
    }


    /**
     * 验证登录
     */
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult login(HttpServletRequest request, HttpServletResponse response) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            Account current_account = accountService.getAccountByUsername(username);
            if(current_account != null) {
                String pwd = MD5.md5(QexzConst.MD5_SALT+password);
                if(pwd.equals(current_account.getPassword())) {
                    //设置单位为秒，设置为-1永不过期
                    //request.getSession().setMaxInactiveInterval(180*60);    //3小时
                    request.getSession().setAttribute(QexzConst.CURRENT_ACCOUNT,current_account);
                    ajaxResult.setData(current_account);
                } else {
                    return AjaxResult.fixedError(QexzWebError.WRONG_USERNAME_OR_PASSWORD);
                }
            } else {
                return AjaxResult.fixedError(QexzWebError.WRONG_USERNAME_OR_PASSWORD);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return AjaxResult.fixedError(QexzWebError.COMMON);
        }
        return ajaxResult;
    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout", method= RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        request.getSession().setAttribute(QexzConst.CURRENT_ACCOUNT,null);
        String url=request.getHeader("Referer");
        LOG.info("url = " + url);
        return "redirect:/";
    }

    /**
     * 上传头像
     */
    @RequestMapping(value = "/api/uploadAvatar", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadAvatar(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            //原始名称
            String oldFileName = file.getOriginalFilename(); //获取上传文件的原名
            //存储图片的物理路径
            String file_path = QexzConst.UPLOAD_FILE_IMAGE_PATH;
            LOG.info("file_path = " + file_path);
            //上传图片
            if(file!=null && oldFileName!=null && oldFileName.length()>0){
                //新的图片名称
                String newFileName = UUID.randomUUID() + oldFileName.substring(oldFileName.lastIndexOf("."));
                //新图片
                File newFile = new File(file_path+newFileName);
                //将内存中的数据写入磁盘
                file.transferTo(newFile);
                //将新图片名称返回到前端
                ajaxResult.setData(newFileName);
            }else{
                return AjaxResult.fixedError(QexzWebError.UPLOAD_FILE_IMAGE_NOT_QUALIFIED);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return AjaxResult.fixedError(QexzWebError.UPLOAD_FILE_IMAGE_ANALYZE_ERROR);
        }
        return ajaxResult;
    }

    /**
     * API:添加用户
     */
    @RequestMapping(value="/api/addAccount", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult addAccount(@RequestBody Account account) {
        AjaxResult ajaxResult = new AjaxResult();
        Account existAccount = accountService.getAccountByUsername(account.getUsername());
        if(existAccount == null) {//检测该用户是否已经注册
            account.setPassword(MD5.md5(QexzConst.MD5_SALT+account.getPassword()));
            account.setAvatarImgUrl(QexzConst.DEFAULT_AVATAR_IMG_URL);
            account.setDescription("");
            int accountId = accountService.addAccount(account);
            return new AjaxResult().setData(accountId);
        }
        return AjaxResult.fixedError(QexzWebError.AREADY_EXIST_USERNAME);
    }

    /**
     * API:更新用户
     */
    @RequestMapping(value="/api/updateManegeAccount", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateAccount(@RequestBody Account account) {
        AjaxResult ajaxResult = new AjaxResult();
        account.setPassword(MD5.md5(QexzConst.MD5_SALT+account.getPassword()));
        boolean result = accountService.updateAccount(account);
        return new AjaxResult().setData(result);
    }

    /**
     * API:删除用户
     */
    @DeleteMapping("/api/deleteAccount/{id}")
    @ResponseBody
    public AjaxResult deleteAccount(@PathVariable int id) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean accresult = accountService.deleteAccount(id);
        boolean mycresult = myClassService.deleteByMyClassStudentId(id);
        boolean graresult = gradeService.deleteGradesByStudentId(id);
        return new AjaxResult().setData(accresult && mycresult && graresult);
    }


    /**
     * 批量导入用户
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/api/addAccounts",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addAccounts(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception{
        List<Account> accounts = new ArrayList<>();
        try{
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheetAt = workbook.getSheetAt(0);
            for(Row row:sheetAt){
                //首行（即表头）不读取
                if (row.getRowNum() == 0) {
                    continue;
                }
                Account account = new Account();
                //获取用户名字
                String name = row.getCell(0).getStringCellValue();
                account.setName(name);
                //用户名
                String username = row.getCell(1).getStringCellValue();
                account.setUsername(username);
                //初始化密码
                String password = MD5.md5("123456");
                account.setPassword(password);
                account.setCreateTime(new Date());
                Account curAccount = (Account) request.getSession().getAttribute(QexzConst.CURRENT_ACCOUNT);
                int level = curAccount.getLevel() - 1;
                account.setLevel(level);
                accounts.add(account);
            }
            //关闭流
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        int flag = 0;
        for(Account account:accounts){
            flag += accountService.addAccount(account);
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
    @ResponseBody
    public void downLoad(HttpServletRequest request, HttpServletResponse response) throws Exception{

        //excel文件名
        String fileName = "批量导入用户模板.xlsx";

        //sheet名
        String sheetName = "用户模板";

        //excel标题
        String[] title = {"名字","账号(一般为学号或者教工号)"};

        String[][] content = {{"啊祖","2017414141"}};
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
     * API:禁用账号
     */
    @RequestMapping(value="/api/disabledAccount/{id}", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult disabledAccount(@PathVariable int id) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = accountService.disabledAccount(id);
        return new AjaxResult().setData(result);
    }

    /**
     * API:解禁账号
     */
    @RequestMapping(value="/api/abledAccount/{id}", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult abledAccount(@PathVariable int id) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = accountService.abledAccount(id);
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
