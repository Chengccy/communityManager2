package com.nk.mapper;

import com.nk.entity.LoginLogCount;
import com.nk.entity.LoginLogPOVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoginLogMapper {
    List<LoginLogPOVO> getLoginLog(@Param("companyId") Integer companyId, @Param("departmentId") Integer departmentId, @Param("name") String name, @Param("username") String username, @Param("start") String start, @Param("end") String end);
    Boolean deleteLoginLog(@Param("idStr") String idStr);
    List<LoginLogCount> getLoginLogCount(@Param("companyId") Integer companyId, @Param("departmentId") Integer departmentId, @Param("name") String name, @Param("username") String username, @Param("start") String start, @Param("end") String end);
    Boolean insertLoginLog(@Param("sessionId") String sessionId, @Param("time")String time, @Param("userId")Integer userId);
    Boolean updateLoginLog(@Param("sessionId") String sessionId,@Param("time")String time);
    String getPreLoginTimeByUserId(@Param("userId") Integer userId);
}