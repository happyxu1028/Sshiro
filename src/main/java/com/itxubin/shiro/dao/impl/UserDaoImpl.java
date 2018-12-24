package com.itxubin.shiro.dao.impl;

import com.itxubin.shiro.dao.UserDao;
import com.itxubin.shiro.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao {


    @Autowired
    private JdbcTemplate    jdbcTemplate;

    @Override
    public User getPasswordByUsername(String userName) {
        String sql = "select username,password from users where username = ?";
        List<User> list = jdbcTemplate.query(sql, new String[]{userName}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setPassword(resultSet.getString("password"));
                user.setUsername(resultSet.getString("username"));
                return user;
            }
        });

        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @Override
    public Set<String> getUserRolesByUsername(String username) {
        String sql = "select role_name from user_roles where username = ?";
        List<String> list = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("role_name");
            }
        });

        Set<String> set = new HashSet<>(list);
        return set;
    }

    @Override
    public Set<String> getPermissionsByRoleName(String username) {
        String sql = "select permission from roles_permissions where role_name = ?";
        List<String> list = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("permission");
            }
        });

        Set<String> set = new HashSet<>(list);
        return set;
    }
}
