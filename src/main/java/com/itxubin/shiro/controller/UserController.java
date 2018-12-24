package com.itxubin.shiro.controller;

import com.itxubin.shiro.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {


    @RequestMapping(value = "/subLogin",method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object subLogin(User user){

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
            subject.checkRole("admin");
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
        return "登录成功";
    }
}
