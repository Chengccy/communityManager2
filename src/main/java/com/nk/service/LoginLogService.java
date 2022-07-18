package com.nk.service;

import com.nk.entity.LoginLogCount;
import com.nk.entity.LoginLogPOVO;
import com.nk.mapper.LoginLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginLogService {
    @Autowired
    private LoginLogMapper loginLogMapper;

    public Boolean deleteLoginLog(String idStr) {
        return loginLogMapper.deleteLoginLog(idStr);
    }

    public List<LoginLogCount> getLoginLogCount(Integer companyId, Integer departmentId, String name, String username, String start, String end) {
        return loginLogMapper.getLoginLogCount(companyId,departmentId,name,username,start,end);
    }
    public List<LoginLogPOVO> getLoginLog(Integer companyId, Integer departmentId, String name, String username, String start, String end) {
        return loginLogMapper.getLoginLog(companyId,departmentId,name,username,start,end);
    }

    public String getPreLoginTimeByUserId(int userId) {
        return loginLogMapper.getPreLoginTimeByUserId(userId);
    }

}
