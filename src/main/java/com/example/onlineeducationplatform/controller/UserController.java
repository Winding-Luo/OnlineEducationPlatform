package com.example.onlineeducationplatform.controller;

import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 获取所有用户
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 获取单个用户
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // 注册用户
    @PostMapping("/register")
    public User handleRegistration(@RequestBody User user) {
        userService.insertUser(user);
        return user; // ID 已回填
    }

    // 更新用户
    @PutMapping("/{id}")
    public User handleUpdate(@PathVariable int id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return user;
    }

    // 删除用户 (需要 admin 角色)
    @RequiresRoles("admin")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> handleDelete(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<User> handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token); // Shiro 完成密码校验
            // 登录成功，直接查询用户信息返回
            User user = userService.selectUserByUsername(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // 登出接口
    @PostMapping("/logout")
    public ResponseEntity<Void> handleLogout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // == Shiro 跳转辅助接口 ==

    // 未登录时跳转到这里，返回 401，让前端拦截器处理
    @GetMapping("/unauthorized")
    public ResponseEntity<String> handleUnauthorized() {
        return new ResponseEntity<>("未认证，请先登录", HttpStatus.UNAUTHORIZED);
    }

    // 权限不足时跳转到这里，返回 403
    @GetMapping("/forbidden")
    public ResponseEntity<String> handleForbidden() {
        return new ResponseEntity<>("权限不足", HttpStatus.FORBIDDEN);
    }

    // 捕获权限异常
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAuthzException(AuthorizationException e) {
        return new ResponseEntity<>("权限不足: " + e.getMessage(), HttpStatus.FORBIDDEN);
    }
}