package com.example.onlineeducationplatform.controller;

import com.example.onlineeducationplatform.model.User;
import com.example.onlineeducationplatform.service.UserService;
import org.apache.shiro.SecurityUtils; // <-- 导入 Shiro
import org.apache.shiro.authc.AuthenticationException; // <-- 导入 Shiro 异常
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions; // <-- 导入权限注解
import org.apache.shiro.authz.annotation.RequiresRoles; // <-- 导入角色注解
import org.apache.shiro.subject.Subject; // <-- 导入 Shiro Subject
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import javax.servlet.http.HttpSession; // <-- 不再需要 HttpSession
import java.util.List;
import java.util.Map;

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
    @RequiresRoles("admin")
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
            @RequestParam("password") String password) {

        // 1. 获取当前用户 (Subject)
        Subject subject = SecurityUtils.getSubject();

        // 2. 创建用户名/密码令牌
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            // 3. 执行登录
            subject.login(token);

            // 4. 登录成功
            // 从数据库中获取完整的 User 对象 (密码是 @JsonIgnore 的)
            User user = userService.login(username, null);
            // Shiro 默认会使用 Session，所以登录状态会保持
            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (UnknownAccountException e) {
            // 用户名不存在
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (IncorrectCredentialsException e) {
            // 密码错误
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (AuthenticationException e) {
            // 其他登录失败
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // 9. (修改) 处理登出 (POST /api/users/logout)
    // 使用 POST /logout 更符合 REST 规范
    @PostMapping("/logout")
    public ResponseEntity<Void> handleLogout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout(); // Shiro 登出
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ===== (新增) Shiro 相关的辅助端点 =====

    // 对应 ShiroFilterFactoryBean 中的 loginUrl
    @GetMapping("/unauthorized")
    public ResponseEntity<String> handleUnauthorized() {
        // 当用户未登录访问受限资源时，Shiro 会重定向到这里
        return new ResponseEntity<>("未认证，请先登录", HttpStatus.UNAUTHORIZED);
    }

    // 对应 ShiroFilterFactoryBean 中的 unauthorizedUrl
    @GetMapping("/forbidden")
    public ResponseEntity<String> handleForbidden() {
        // 当用户权限不足时，Shiro 会重定向到这里
        return new ResponseEntity<>("权限不足", HttpStatus.FORBIDDEN);
    }

    // ===== (新增) 权限异常处理器 =====
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAuthzException(AuthorizationException e) {
        // 当 @RequiresRoles 或 @RequiresPermissions 检查失败时
        return new ResponseEntity<>("权限不足: " + e.getMessage(), HttpStatus.FORBIDDEN);
    }
}