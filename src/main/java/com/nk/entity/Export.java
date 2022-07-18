package com.nk.entity;

import com.nk.mapper.LoginLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Export {
    public static List<LoginLogPOVO> loginLogPOVOList;
    public static List<LoginLogCount> loginLogCountList;
    public static List<StaffCount> staffCountList;
    public static List<OperationInfo> operationInfoList;
    public static LoginLogMapper logMapper;
    @Autowired
    public  LoginLogMapper loginLogMapper;
    @PostConstruct
    public void beforeInit() {
       logMapper=loginLogMapper;
    }
}
