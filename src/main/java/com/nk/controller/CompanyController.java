package com.nk.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nk.entity.Company;
import com.nk.entity.JsonResult;
import com.nk.entity.OperationInfo;
import com.nk.mapper.OperationInfoMapper;
import com.nk.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;
    @Autowired
    OperationInfoMapper operationInfoMapper;
    @RequestMapping("/getAllCompany")
    public List<Company> getAllCompany(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Company> companyList = companyService.getAllCompany();
        return companyList;
    }
    @RequestMapping("/deleteCompanyById")
    public JsonResult deleteCompanyById(String idStr, HttpSession session) throws IOException {
        String username = (String) session.getAttribute("username");
        List<Company> companyList=companyService.getCompanyByIdStr(idStr);
        OperationInfo operationInfo=new OperationInfo("company_t","删除", JSONObject.toJSONString(companyList),"空",username,"操作成功");
        Boolean addResult=operationInfoMapper.addOperationInfo(operationInfo);
        Boolean result = companyService.deleteCompanyById(idStr);
        JsonResult<Company> jsonResult = new JsonResult<>();
        if (result) {
            jsonResult.setCode(200);
        }
        return jsonResult;
    }
    @RequestMapping("/addCompany")
    public JsonResult addCompany(Company company,HttpSession session)  {
        String username = (String) session.getAttribute("username");
        JsonResult<Company> jsonResult=new JsonResult<>();
        companyService.addCompany(company);
        //
        Company company1=companyService.getCompanyByName(company.getCompanyName()).get(0);
        OperationInfo operationInfo=new OperationInfo("company_t","新增","空",JSONObject.toJSONString(company),username,"操作成功");
        Boolean addResult=operationInfoMapper.addOperationInfo(operationInfo);
        jsonResult.setCode(200);
        return jsonResult;
    }
    @RequestMapping("/getCompany")
    public JsonResult getCompany(String companyName, int page, int limit) {
        PageHelper.startPage(page,limit);
        List<Company> companyList = companyService.getCompanyByName(companyName);
        PageInfo<Company> pageInfo=new PageInfo<>(companyList);
        JsonResult<Company> jsonResult=new JsonResult<>();
        jsonResult.setCount(pageInfo.getTotal());
        jsonResult.setData(pageInfo.getList());
        jsonResult.setCode(200);
        return jsonResult;
    }
    @RequestMapping("/modifyCompany")
    public JsonResult modifyCompany(Company company,HttpSession session) {
        String username = (String) session.getAttribute("username");
        Company company1=companyService.getCompanyById(company.getCompanyId()).get(0);
        String before=JSONObject.toJSONString(company1);
        //
        JsonResult<Company> jsonResult = new JsonResult<>();
        Boolean result = companyService.modifyCompany(company);
        //
        Company company2=companyService.getCompanyById(company.getCompanyId()).get(0);
        String after=JSONObject.toJSONString(company2);
        OperationInfo operationInfo=new OperationInfo("company_t","修改",before,after,username,"操作成功");
        Boolean addResult=operationInfoMapper.addOperationInfo(operationInfo);
        if(result){
            jsonResult.setCode(200);
        }
        return jsonResult;
    }
    @RequestMapping("/getCompanyById")
    public JsonResult<Company> getCompanyId(Integer companyId){
        List<Company> companyList=companyService.getCompanyById(companyId);
        JsonResult<Company> jsonResult=new JsonResult<>();
        jsonResult.setCode(200);
        jsonResult.setData(companyList);
        return jsonResult;
    }
    @RequestMapping("/getAllCompanyAdd")
    public List<Company> getAllCompanyAdd() {
        List<Company> companyList = companyService.getAllCompanyAdd();
        return companyList;
    }
}
