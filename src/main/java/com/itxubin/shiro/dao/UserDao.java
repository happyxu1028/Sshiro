package com.itxubin.shiro.dao;

import com.itxubin.shiro.vo.User;

import java.util.Set;

public interface UserDao {


    User getPasswordByUsername(String userName);


    Set<String> getUserRolesByUsername(String username);


    Set<String> getPermissionsByRoleName(String username);
}
