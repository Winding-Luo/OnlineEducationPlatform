package com.example.onlineeducationplatform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private int id;
    private String username;

    // !!关键修改!!:
    // 使用 WRITE_ONLY 属性：允许前端传入(反序列化)，但后端返回JSON时隐藏(序列化)
    // 替代之前的 @JsonIgnore (它会导致前后端都忽略，从而接收不到密码)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}