package com.example.onlineeducationplatform.service;

import com.example.onlineeducationplatform.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User getUserById(int id);
    // (你可以根据需要添加其他方法)
    void insertUser(User user);
    void updateUser(User user); // <-- 添加这一行
    void deleteUser(int id);  // <-- 添加这一行
    User login(String username, String password); // <-- 添加这一行
    List<User> getAllUsers(); // <-- 添加这一行
    //用于Shiro的新方法
    Set<String> findRoles(String username);
    Set<String> findPermissions(String username);
}