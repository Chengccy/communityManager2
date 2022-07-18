package com.nk.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nk.entity.*;
import com.nk.mapper.OperationInfoMapper;
import com.nk.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @Autowired
    OperationInfoMapper operationInfoMapper;
    @RequestMapping("/getDepartmentByCompanyId")
    public List<Department> getDepartmentByCompanyId(Integer companyId) throws IOException {
        List<Department> departmentList= departmentService.getDepartmentByCompanyId(companyId);
        return departmentList;
    }
    @RequestMapping("/getDepartment")
    public JsonResult getDepartment(Integer companyId, int page, int limit) throws IOException {
        JsonResult<DepartmentPOVO> jsonResult=new JsonResult<>();
        PageHelper.startPage(page,limit);
        List<DepartmentPOVO> departmentPOVOList=departmentService.getDepartment(companyId);
        PageInfo<DepartmentPOVO> pageInfo=new PageInfo<>(departmentPOVOList);
        jsonResult.setData(pageInfo.getList());
        jsonResult.setCode(200);
        jsonResult.setCount(pageInfo.getTotal());
        return jsonResult;
    }
    //添加部门
    @RequestMapping("/addDepartment")
    public JsonResult addDepartment(Department department, HttpSession session){
        String username = (String) session.getAttribute("username");
        Boolean result=departmentService.addDepartment(department);
        //
        Department department1=departmentService.getLastDepartment();
        OperationInfo operationInfo=new OperationInfo("department_t","新增","空",JSONObject.toJSONString(department1),username,"操作成功");
        Boolean addResult=operationInfoMapper.addOperationInfo(operationInfo);
        JsonResult<Department> jsonResult=new JsonResult<>();
        if(result){
            jsonResult.setCode(200);
        }
        return jsonResult;
    }
    //删除部门
    @RequestMapping("/deleteDepartment")
    public JsonResult deleteDepartment(String idStr,HttpSession session) {
        String username = (String) session.getAttribute("username");
        List<Department> departmentList=departmentService.getDepartmentByIdStr(idStr);
        OperationInfo operationInfo=new OperationInfo("department_t","删除", JSONObject.toJSONString(departmentList),"空",username,"操作成功");
        Boolean addResult=operationInfoMapper.addOperationInfo(operationInfo);
        //
        Boolean result=departmentService.deleteDepartment(idStr);
        JsonResult jsonResult=new JsonResult();
        if(result){
            jsonResult.setCode(200);
        }
        return jsonResult;
    }
    //修改部门
    @RequestMapping("/modifyDepartment")
    public JsonResult modifyDepartment(Department department,HttpSession session){
        String username = (String) session.getAttribute("username");
        Department department1=departmentService.getDepartmentByIdStr(department.getDepartmentId().toString()).get(0);
        String before=JSONObject.toJSONString(department1);
        //
        JsonResult<Department> jsonResult = new JsonResult<>();
        Boolean result=departmentService.modifyDepartment(department);
        //
        Department department2=departmentService.getDepartmentByIdStr(department.getDepartmentId().toString()).get(0);
        String after=JSONObject.toJSONString(department2);
        OperationInfo operationInfo=new OperationInfo("department_t","修改",before,after,username,"操作成功");
        Boolean addResult=operationInfoMapper.addOperationInfo(operationInfo);
        if(result){
            jsonResult.setCode(200);
        }
        return jsonResult;
    }
    //人员统计
    @RequestMapping("/getStaffCount")
    public JsonResult  getStaffCount(Integer departmentId, Integer companyId, int page, int limit) throws IOException {
        PageHelper.startPage(page,limit);
        List<StaffCount> staffCountList=departmentService.getStaffCount(companyId,departmentId);
        PageInfo<StaffCount> pageInfo=new PageInfo<>(staffCountList);
        //
        Export.staffCountList=departmentService.getStaffCount(companyId,departmentId);
        JsonResult<StaffCount> jsonResult=new JsonResult<>();
        jsonResult.setCode(200);
        jsonResult.setCount(pageInfo.getTotal());
        jsonResult.setData(pageInfo.getList());
        return jsonResult;
    }
    @RequestMapping("/getDepartmentByCompanyIdAdd")
    public List<Department> getDepartmentByCompanyIdAdd(Integer companyId){
        List<Department> departmentList= departmentService.getDepartmentByCompanyIdAdd(companyId);
        return departmentList;
    }
}
