package com.example.onlineeducationplatform.service;

import com.example.onlineeducationplatform.mapper.UserMapper;
import com.example.onlineeducationplatform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 确保导入事务注解

import java.util.List;

// 1. 必须使用 @Service 注解标记 [cite: 263]
// 2. 必须实现 UserService 接口
@Service
public class UserServiceImpl implements UserService {

    // 3. 必须注入 UserMapper [cite: 265]
    @Autowired
    private UserMapper userMapper;

    // 4. 必须实现接口中定义的方法 [cite: 267-270]
    @Override
    @Transactional(readOnly = true) // 建议添加事务注解
    public User getUserById(int id) {
        return userMapper.selectUserById(id);
    }

    // 实现其他方法...
    @Override
    @Transactional // 默认是读写事务
    public void insertUser(User user) {
        // 你可以在这里添加业务逻辑，比如检查用户名是否已存在
        userMapper.insertUser(user);
    }

    // ↓↓↓↓ 添加下面的新方法 ↓↓↓↓
    @Override
    @Transactional
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userMapper.deleteUser(id);
    }
    // ↑↑↑↑ 添加上面的新方法 ↑↑↑↑
    // ↓↓↓↓ 添加下面的新方法 ↓↓↓↓
    @Override
    @Transactional(readOnly = true)
    public User login(String username, String password) {
        User user = userMapper.selectUserByUsername(username);

        // 检查用户是否存在
        if (user != null) {
            // 检查密码是否匹配 (注意：真实项目应使用加密密码)
            if (user.getPassword().equals(password)) {
                return user; // 登录成功，返回用户信息
            }
        }
        return null; // 登录失败
    }
    // ↑↑↑↑ 添加上面的新方法 ↑↑↑↑
    @Override
    public List<User> getAllUsers() {
        // 调用 mapper 中我们刚刚在 XML 里定义的 "findAll" 方法
        return userMapper.findAll();
    }
}