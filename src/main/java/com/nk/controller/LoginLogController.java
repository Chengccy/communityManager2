package com.nk.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nk.entity.*;
import com.nk.mapper.LoginLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/loginLog")
public class LoginLogController {
    @Autowired
    LoginLogMapper loginLogMapper;

    @RequestMapping("/getLoginLog")
    public JsonResult getLoginLog(Integer companyId, Integer departmentId, String name, String username, String start, String end, int page, int limit){
        PageHelper.startPage(page,limit);
        List<LoginLogPOVO> loginLogPOVOList=loginLogMapper.getLoginLog(companyId,departmentId,name,username,start,end);
        PageInfo<LoginLogPOVO> pageInfo=new PageInfo<>(loginLogPOVOList);
        //保存查询结果
        Export.loginLogPOVOList=loginLogMapper.getLoginLog(companyId,departmentId,name,username,start,end);
        JsonResult<LoginLogPOVO> jsonResult=new JsonResult<>();
        jsonResult.setCode(200);
        jsonResult.setCount(pageInfo.getTotal());
        jsonResult.setData(pageInfo.getList());
        return jsonResult;
    }
    @RequestMapping("/deleteLoginLog")
    public JsonResult deleteLoginLog(String idStr) throws IOException {
        Boolean result=loginLogMapper.deleteLoginLog(idStr);
        JsonResult<User> jsonResult=new JsonResult<>();
        if(result){
            jsonResult.setCode(200);
        }
        return jsonResult;
    }
    @RequestMapping("/getLoginLogCount")
    public JsonResult getLoginLogCount(Integer companyId, Integer departmentId, String name, String username, String start, String end,
                                                      @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10")int limit) throws IOException {
        PageHelper.startPage(page,limit);
        List<LoginLogCount> loginLogCountList=loginLogMapper.getLoginLogCount(companyId,departmentId,name,username,start,end);
        PageInfo<LoginLogCount> pageInfo=new PageInfo<>(loginLogCountList);
        //
        Export.loginLogCountList=loginLogMapper.getLoginLogCount(companyId,departmentId,name,username,start,end);
        JsonResult<LoginLogCount> jsonResult=new JsonResult<>();
        jsonResult.setCount(pageInfo.getTotal());
        jsonResult.setCode(200);
        jsonResult.setData(pageInfo.getList());
        return jsonResult;
    }
}
