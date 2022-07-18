package com.nk.mapper;

import com.nk.entity.User;
import com.nk.entity.UserPOVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper {

    User queryUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    List<UserPOVO> getUser(@Param("name") String name, @Param("departmentId") Integer departmentId, @Param("companyId") Integer companyId);

    Boolean addUser(User user);

    Boolean deleteUser(@Param("idStr")String idStr);

    Boolean modifyUser(User user);


    List<User> getUserByIdStr(@Param("idStr") String idStr);

    User getLastUser();
}