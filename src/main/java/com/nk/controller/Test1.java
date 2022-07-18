package com.nk.controller;
import com.nk.entity.User;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Test1 {
    @RequestMapping("/test")
    @ResponseBody
    public String test(@RequestParam(defaultValue = "cheng") String name){
        return name;
    }
    @RequestMapping("/")
    public String toLogin(HttpSession session){
        if(session.getAttribute("user")!=null){
            return "index";
        }
        return "/pages/user/login";
    }

    @Test
    public void test(){
        List<User> list=new ArrayList<>();
        User user=new User();
        User user1=new User();
        user.setUsername("cheng");
        list.add(user);
        list.add(user1);
        System.out.println(list);
    }
}
