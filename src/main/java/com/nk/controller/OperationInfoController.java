package com.nk.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nk.entity.Export;
import com.nk.entity.JsonResult;
import com.nk.entity.OperationInfo;
import com.nk.mapper.OperationInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/operationInfo")
public class OperationInfoController {
    @Autowired
    OperationInfoMapper operationInfoMapper;

    //查询操作日志
    @RequestMapping("/getOperationInfo")
    public JsonResult getOperationInfo(String tableName,String operator,String type,String start,String end,int page,int limit) throws IOException {
        PageHelper.startPage(page,limit);
        List<OperationInfo> operationInfoList=operationInfoMapper.getOperationInfo(tableName,type,operator,start,end);
        PageInfo<OperationInfo> pageInfo=new PageInfo<>(operationInfoList);
        //
        Export.operationInfoList=operationInfoMapper.getOperationInfo(tableName,type,operator,start,end);
        JsonResult<OperationInfo> jsonResult=new JsonResult<>();
        jsonResult.setCount(pageInfo.getTotal());
        jsonResult.setCode(200);
        jsonResult.setData(pageInfo.getList());
        return jsonResult;
    }
}
