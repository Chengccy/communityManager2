package com.nk.service;

import com.nk.entity.OperationInfo;
import com.nk.mapper.OperationInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationInfoService {
    @Autowired
    OperationInfoMapper operationInfoMapper;
    public List<OperationInfo> getOperationInfo(String tableName, String type, String operator, String start, String end) {
        return operationInfoMapper.getOperationInfo(tableName,type,operator,start,end);
    }
}
