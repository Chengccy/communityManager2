package com.nk.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nk.entity.*;
import com.nk.mapper.OperationInfoMapper;
import com.nk.service.LoginLogService;
import com.nk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import yanzhengma.Verify;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    LoginLogService loginLogService;
    @Autowired
    OperationInfoMapper operationInfoMapper;
    public static Map<String,String> online=new HashMap<>();

    /**
     *
     * 用户登录
     *
     * */
    @RequestMapping("/login")
    protected String login(HttpServletRequest req, HttpServletResponse resp, String username, String password, String verifyCode) throws ServletException, IOException {

        if (req.getSession().getAttribute("user") != null) {
            return "index";
        } else {
            //  1、获取请求的参数
            String vCode = (String) req.getSession().getAttribute("vCode");
            if (vCode.equalsIgnoreCase(verifyCode)) {
                // 调用 userService.login()登录处理业务
                User user = userService.login(new User(username, password));
                // 如果等于null,说明登录 失败!
                if (user == null) {
                    // 把错误信息，和回显的表单项信息，保存到Request域中
                    req.setAttribute("msg", "用户或密码错误！");
                    req.setAttribute("username", username);
                    //   跳回登录页面
                    return "/pages/user/login";
                } else {
                    if (user.getStatus() == 0) {
                        req.setAttribute("msg", "账号已被禁用");
                        return "/pages/user/login";
                    } else {
                        int userId = user.getUserId();
                        String oldLoginTime = loginLogService.getPreLoginTimeByUserId(userId);
                        Object o = online.get(req.getSession().getId());
                        if(o==null){
                            // 登录 成功
                            online.put(req.getSession().getId(),"");
                            HttpSession session = req.getSession();
                            session.setAttribute("username", username);
                            session.setAttribute("userId", userId);
                            session.setAttribute("user", user);
                            session.setAttribute("oldLoginTime", oldLoginTime);
                            Cookie c = new Cookie("JSESSIONID", URLEncoder.encode(session.getId(), "utf-8"));
                            c.setPath("/");
                            c.setMaxAge(2 * 60 * 60);
                            resp.addCookie(c);
                            return "index";
                        }else{
                            req.setAttribute("msg", "账号已登录！");
                            return "/pages/user/login";
                        }

                    }
                }
            } else {
                req.setAttribute("msg", "验证码错误");
                return "/pages/user/login";
            }
        }


    }

    /**
     *
     * 注销用户
     *
     */
    @RequestMapping("/logout")
    protected void logout(HttpServletRequest req, HttpServletResponse resp) {
        online.remove(req.getSession().getId());
        req.getSession().invalidate();
        try {
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成验证码
     */
    @RequestMapping("/createVerifyCode")
    public void createVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        // 生成了一个 验证码
        try {
            Verify.getVirify(request, response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 查询用户
     * */
    @RequestMapping("/getUser")
    @ResponseBody
    public JsonResult getUser(String name, Integer departmentId, Integer companyId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) throws IOException {
        JsonResult<UserPOVO> jsonResult = new JsonResult<>();
        PageHelper.startPage(page, limit);
        List<UserPOVO> userPOVOList = userService.getUser(name, departmentId, companyId);
        PageInfo<UserPOVO> pageInfo = new PageInfo(userPOVOList);
        List<UserPOVO> list = pageInfo.getList();
        jsonResult.setCode(200);
        jsonResult.setData(list);
        jsonResult.setCount(pageInfo.getTotal());
        return jsonResult;
    }

    /**
     * 删除用户
     */
    @RequestMapping("/deleteUser")
    @ResponseBody
    public JsonResult deleteUser(String idStr,HttpSession session)  {
        JsonResult<User> jsonResult = new JsonResult<>();
        String userId= session.getAttribute("userId").toString();
        String[] split = idStr.split(",");
        for(int i=0;i<split.length;i++){
            if(split[i].equals(userId)){
                jsonResult.setMsg("禁止操作");
                jsonResult.setCode(201);
                return  jsonResult;
            }
        }
        String username = (String) session.getAttribute("username");
        List<User> userList=userService.getUserByIdStr(idStr);
        OperationInfo operationInfo=new OperationInfo("user_t","删除", JSONObject.toJSONString(userList),"空",username,"操作成功");
        Boolean addResult=operationInfoMapper.addOperationInfo(operationInfo);
        Boolean result = userService.deleteUser(idStr);
        if(result){
            jsonResult.setCode(200);
        }
        return jsonResult;
    }

    /**
     * 添加用户
     */
    @RequestMapping("/addUser")
    @ResponseBody
    public JsonResult upload(HttpServletRequest request){
        String username= (String) request.getSession().getAttribute("username");
        User user=new User();
        List<String> images=new ArrayList<>();
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        user.setName(params.getParameter("name"));
        user.setUsername(params.getParameter("username"));
        user.setPassword(params.getParameter("password"));
        user.setStatus(Integer.parseInt(params.getParameter("status")));
        user.setEmail(params.getParameter("email"));
        user.setDepartmentId(Integer.parseInt(params.getParameter("departmentId")));
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    //文件名字
                    String filename = file.getOriginalFilename();
                    //文件后缀
                    String fExt = filename.substring(filename.lastIndexOf("."));
                    //为了避免重复 改名
                    String newName = UUID.randomUUID().toString().replaceAll("-", "") + fExt;
                    byte[] bytes = file.getBytes();
                    String path = "D:\\项目\\practice02\\src\\main\\webapp\\upload\\" + newName;
                    stream = new BufferedOutputStream(new FileOutputStream(
                            new File(path)));
                    stream.write(bytes);
                    images.add("upload/"+newName);
                    stream.close();
                } catch (Exception e) {
                    stream = null;
                }
            }
        }
        user.setImages(images.toString().substring(1, images.toString().length() - 1));
        Boolean result=userService.addUser(user);
        //
        User user1=userService.getLastUser();
        OperationInfo operationInfo=new OperationInfo("user_t","新增","空",JSONObject.toJSONString(user1),username,"操作成功");
        Boolean addResult=operationInfoMapper.addOperationInfo(operationInfo);
        JsonResult jsonResult=new JsonResult();
        if(result){
            jsonResult.setCode(200);
        }
        return jsonResult;
    }

    /**
     * 修改用户
     * */
    @RequestMapping("/modifyUser")
    @ResponseBody
    public JsonResult modifyUser(HttpServletRequest request){
        User user1 = (User) request.getSession().getAttribute("user");
        User user=new User();
        List<String> images=new ArrayList<>();
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        user.setName(params.getParameter("name"));
        user.setUsername(params.getParameter("username"));
        user.setPassword(params.getParameter("password"));
        user.setStatus(Integer.parseInt(params.getParameter("status")));
        user.setEmail(params.getParameter("email"));
        user.setDepartmentId(Integer.parseInt(params.getParameter("departmentId")));
        user.setUserId(Integer.parseInt(params.getParameter("userId")));
        String []image=params.getParameterValues("image");
        if(image!=null&&image.length!=0){
            for(int i=0;i<image.length;i++){
                images.add(image[i]);
            }
        }
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    //文件名字
                    String filename = file.getOriginalFilename();
                    //文件后缀
                    String fExt = filename.substring(filename.lastIndexOf("."));
                    //为了避免重复 改名
                    String newName = UUID.randomUUID().toString().replaceAll("-", "") + fExt;
                    byte[] bytes = file.getBytes();
                    String path = "D:\\项目\\practice02\\src\\main\\webapp\\upload\\" + newName;
                    stream = new BufferedOutputStream(new FileOutputStream(
                            new File(path)));
                    stream.write(bytes);
                    images.add("upload/"+newName);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        user.setImages(images.toString().substring(1, images.toString().length() - 1));
            if (user1.getUserId() == user.getUserId() && user.getStatus() == 0) {
                JsonResult jsonResult = new JsonResult();
                jsonResult.setCode(201);
                jsonResult.setMsg("禁止操作！");
                return jsonResult;
            } else {
                String username= (String) request.getSession().getAttribute("username");
                User user2=userService.getUserByIdStr(user.getUserId().toString()).get(0);
                String before=JSONObject.toJSONString(user2);
                //
                Boolean result = userService.modifyUser(user);
                //
                User user3=userService.getUserByIdStr(user.getUserId().toString()).get(0);
                String after=JSONObject.toJSONString(user3);
                OperationInfo operationInfo=new OperationInfo("user_t","修改",before,after,username,"操作成功");
                Boolean addResult=operationInfoMapper.addOperationInfo(operationInfo);

                JsonResult<User> jsonResult = new JsonResult<>();
                if (result) {
                    jsonResult.setCode(200);
                }
                return jsonResult;
            }
    }

    //导出登录详情
    @RequestMapping("/exportLoginLog")
    public void exportLoginLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");
        ExportParams exportParams = new ExportParams("登录日志", "登录日志", ExcelType.HSSF);
        ExcelExportUtil.exportExcel(exportParams, LoginLogPOVO.class, Export.loginLogPOVOList).write(response.getOutputStream());
    }
    //导出登录统计
    @RequestMapping("/exportLoginLogCount")
    public void exportLoginLogCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");
        ExportParams exportParams = new ExportParams("登录统计", "登录统计", ExcelType.HSSF);
        ExcelExportUtil.exportExcel(exportParams, LoginLogCount.class, Export.loginLogCountList).write(response.getOutputStream());
    }
    //导出人员统计
    @RequestMapping("/exportStaffCount")
    public void exportStaffCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");
        ExportParams exportParams = new ExportParams("人员统计", "人员统计", ExcelType.HSSF);
        ExcelExportUtil.exportExcel(exportParams, StaffCount.class, Export.staffCountList).write(response.getOutputStream());
    }
    //导出操作日志
    @RequestMapping("/exportOperationInfo")
    public void exportOperationInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=export.xls");
        ExportParams exportParams = new ExportParams("操作日志", "操作日志", ExcelType.HSSF);
        ExcelExportUtil.exportExcel(exportParams, OperationInfo.class, Export.operationInfoList).write(response.getOutputStream());
    }


}
