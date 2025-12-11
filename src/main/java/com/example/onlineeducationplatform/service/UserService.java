package com.example.onlineeducationplatform.service;

import com.example.onlineeducationplatform.model.User;
import java.util.List;
import java.util.Set;

public interface UserService {
    User getUserById(int id);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    List<User> getAllUsers();

    // 关键：提供按用户名查询，供 Controller 登录后调用
    User selectUserByUsername(String username);

    Set<String> findRoles(String username);
    Set<String> findPermissions(String username);
}