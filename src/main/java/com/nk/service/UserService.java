package com.nk.service;


import com.nk.entity.User;
import com.nk.entity.UserPOVO;
import com.nk.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService{
    
    @Autowired
    private UserMapper userMapper;

    public User login(User user) {
        return userMapper.queryUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    public List<UserPOVO> getUser(String name, Integer departmentId, Integer companyId) {
        return userMapper.getUser(name,departmentId,companyId);
    }

    public Boolean addUser(User user) {
        return userMapper.addUser(user);
    }

    public Boolean deleteUser(String idStr) {
        return userMapper.deleteUser(idStr);
    }
    
    public Boolean modifyUser(User user) {
        return userMapper.modifyUser(user);
    }


    public List<User> getUserByIdStr(String idStr) {
        return userMapper.getUserByIdStr(idStr);
    }

    public User getLastUser() {
        return userMapper.getLastUser();
    }
}
