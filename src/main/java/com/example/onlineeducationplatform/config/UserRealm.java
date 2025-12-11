// OnlineEducationPlatform/src/main/java/com/example/onlineeducationplatform/config/UserRealm.java
package com.example.onlineeducationplatform.config;

import com.example.onlineeducationplatform.model.User;
// 导入 UserMapper
import com.example.onlineeducationplatform.mapper.UserMapper;
// import com.example.onlineeducationplatform.service.UserService; // 不再需要
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class UserRealm extends AuthorizingRealm {

    // === (修正) ===
    // 不再注入 UserService
    // @Autowired
    // private UserService userService;

    // 直接注入 UserMapper
    @Autowired
    private UserMapper userMapper;

    /**
     * 授权 (Authorization)
     * 当访问受限资源时 (例如检查 @RequiresRoles), 此方法会被调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 1. 获取当前登录用户名
        String username = (String) principals.getPrimaryPrincipal();

        // 2. === (修正) === 从 UserMapper 直接查询角色和权限
        // Set<String> roles = userService.findRoles(username);
        // Set<String> permissions = userService.findPermissions(username);
        Set<String> roles = userMapper.findRoles(username);
        Set<String> permissions = userMapper.findPermissions(username);


        // 3. 将角色和权限信息交给 Shiro
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);

        return info;
    }

    /**
     * 认证 (Authentication)
     * 当调用 subject.login() 时, 此方法会被调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1. 从 token 中获取用户名
        String username = (String) token.getPrincipal();

        // 2. === (修正) === 使用 UserMapper 从数据库查询用户
        // 我们重用 login 方法，它现在只根据用户名查询
        // User user = userService.login(username, null);
        User user = userMapper.selectUserByUsername(username);

        // 3. 检查用户是否存在
        if (user == null) {
            throw new UnknownAccountException("用户名不存在");
        }

        // 4. 构建 Shiro 需要的 AuthenticationInfo 对象

        // 数据库中存储的加密密码
        String encryptedPassword = user.getPassword();

        // 盐值 (必须与注册时使用的一致)
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());

        // Realm 的名称
        String realmName = getName();

        // Shiro 会自动使用我们在 applicationContext.xml 中配置的 HashedCredentialsMatcher
        // 来比较 (用户输入的token中的密码) 和 (数据库中存储的info中的密码)
        return new SimpleAuthenticationInfo(
                user.getUsername(),     // principal (用户名)
                encryptedPassword,      // credentials (数据库中的加密密码)
                salt,                   // 盐值
                realmName               // Realm 名称
        );
    }
}