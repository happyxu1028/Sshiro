package com.itxubin.shiro.realm;

import com.itxubin.shiro.dao.UserDao;
import com.itxubin.shiro.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {


    @Autowired
    private UserDao     userDao;


    /**
     * 授权
     *
     * @param principals
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        Set<String> rolesSet = getRolesByUsername(username);
        Set<String> permissionSet = getPermissionByRoles(rolesSet);

        AuthorizationInfo result = new SimpleAuthorizationInfo();
        ((SimpleAuthorizationInfo) result).setRoles(rolesSet);
        ((SimpleAuthorizationInfo) result).setStringPermissions(permissionSet);
        return result;
    }

    /**
     * 模拟获取角色数据
     *
     * @param username
     * @return
     */
    private Set<String> getRolesByUsername(String username) {

        return userDao.getUserRolesByUsername(username);
    }

    /**
     * 模拟获取权限数据
     *
     * @param rolesSet
     * @return
     */
    private Set<String> getPermissionByRoles(Set<String> rolesSet) {
        if(CollectionUtils.isEmpty(rolesSet)){
            return null;
        }

        Set<String> set = new HashSet<String>();
        for (String thisRole : rolesSet) {
            set.addAll(userDao.getPermissionsByRoleName(thisRole));
        }
        return set;
    }

    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = getPassWordByName(username);

        if (password == null) {
            return null;
        }

        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username, password, this.getName());

        return authenticationInfo;
    }

    private String getPassWordByName(String userName) {
        User user = userDao.getPasswordByUsername(userName);
        if (user == null){
            return null;
        }

        return user.getPassword();

    }
}
