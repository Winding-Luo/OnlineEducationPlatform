package com.example.onlineeducationplatform.mapper;

import com.example.onlineeducationplatform.model.User;
// import org.apache.ibatis.annotations.Mapper; // 使用 MapperScannerConfigurer 就不需要这个了
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository // 遵循文档中的描述
public interface UserMapper {
    User selectUserById(int id);
    User selectUserByUsername(String username); // <-- 添加这一行
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    List<User> findAll(); // <-- 添加这一行

    //用于权限控制
    Set<String> findRoles(String username);
    Set<String> findPermissions(String username);
}