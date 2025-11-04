package com.example.onlineeducationplatform.controller;

import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // <-- 导入更多注解

import javax.servlet.http.HttpSession;
import java.util.List; // <-- 导入 List
import java.util.Map; // <-- 导入 Map

// 1. 将 @Controller 改为 @RestController
// @RestController = @Controller + @ResponseBody (所有方法默认返回JSON)
@RestController
// 2. 添加统一的API路径前缀，匹配CORS配置
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 3. (新增) 获取所有用户 (GET /api/users)
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 4. (修改) 获取单个用户 (GET /api/users/{id})
    // 移除了 ModelAndView，直接返回 User 对象 (会被转为JSON)
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // 5. (修改) 处理注册 (POST /api/users/register)
    // 使用 @RequestBody 接收前端发送的JSON数据并封装到User对象
    @PostMapping("/register")
    public User handleRegistration(@RequestBody User user) {
        userService.insertUser(user);
        // 假设 insertUser 后 ID 已被回填 (需要MyBatis配置)
        // 返回创建的 User 对象
        return user;
    }

    // 6. (修改) 处理更新 (PUT /api/users/{id})
    // RESTful 风格使用 PUT 进行更新，并用 @PathVariable 传ID
    @PutMapping("/{id}")
    public User handleUpdate(@PathVariable int id, @RequestBody User user) {
        user.setId(id); // 确保ID正确

        // (保持你原来的密码检查逻辑)
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            User oldUser = userService.getUserById(user.getId());
            user.setPassword(oldUser.getPassword());
        }

        userService.updateUser(user);
        return user; // 返回更新后的 User 对象
    }

    // 7. (修改) 处理删除 (DELETE /api/users/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> handleDelete(@PathVariable int id) {
        userService.deleteUser(id);
        // 返回 204 No Content 表示成功
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 8. (修改) 处理登录 (POST /api/users/login)
    // @RequestParam 用于接收 x-www-form-urlencoded 或 multipart-form-data
    // 如果前端发送JSON，应改为 @RequestBody Map<String, String> credentials
    // 这里我们先假设前端用表单数据，或者你可以改为接收JSON
    @PostMapping("/login")
    public ResponseEntity<User> handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session) {

        User user = userService.login(username, password);

        if (user != null) {
            session.setAttribute("loggedInUser", user);
            // 登录成功，返回用户信息
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            // 登录失败，返回 401 Unauthorized
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // 9. (修改) 处理登出 (POST /api/users/logout)
    // 使用 POST /logout 更符合 REST 规范
    @PostMapping("/logout")
    public ResponseEntity<Void> handleLogout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 10. (移除) 移除所有用于显示JSP页面的方法
    // 例如：showRegisterPage(), showEditPage(), showLoginPage()
    // 这些页面现在由 Vue.js 前端负责渲染
}