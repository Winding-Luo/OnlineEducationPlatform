package com.example.onlineeducationplatform.service;

import com.example.onlineeducationplatform.mapper.UserMapper;
import com.example.onlineeducationplatform.model.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return userMapper.selectUserById(id);
    }

    @Override
    @Transactional
    public void insertUser(User user) {
        // 注册加密
        String algorithmName = "SHA-256";
        String source = user.getPassword();
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        int hashIterations = 1024;
        SimpleHash hash = new SimpleHash(algorithmName, source, salt, hashIterations);
        user.setPassword(hash.toHex());

        userMapper.insertUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        // 简单处理：如果有密码则加密，实际需更严谨判断
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String algorithmName = "SHA-256";
            String source = user.getPassword();
            ByteSource salt = ByteSource.Util.bytes(user.getUsername());
            int hashIterations = 1024;
            SimpleHash hash = new SimpleHash(algorithmName, source, salt, hashIterations);
            user.setPassword(hash.toHex());
        }
        userMapper.updateUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userMapper.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User selectUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> findRoles(String username) {
        return userMapper.findRoles(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> findPermissions(String username) {
        return userMapper.findPermissions(username);
    }
}