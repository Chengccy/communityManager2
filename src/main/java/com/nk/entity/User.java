package com.nk.entity;

import com.nk.mapper.LoginLogMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class User implements HttpSessionBindingListener {
    private Integer userId;
    private String name;
    private String username;
    private String password;
    private int departmentId;
    private String createTime;
    private String email;
    private String images;
    private int status;
    private String sessionId;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        LoginLogMapper loginLogMapper = Export.logMapper;
        HttpSession session = event.getSession();
        sessionId = session.getId();
        Integer userId = (Integer) session.getAttribute("userId");
        loginLogMapper.insertLoginLog(sessionId, getCurrDatetimeStr(), userId);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        LoginLogMapper loginLogMapper = Export.logMapper;
        loginLogMapper.updateLoginLog(sessionId, getCurrDatetimeStr());
    }

    private static String getCurrDatetimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


}
