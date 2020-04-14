package com.gxx.springbootjedis.service;

import com.gxx.springbootjedis.entity.User;

import java.util.List;

public interface UserService {
    List<User> selectList();
    User findUserById(int id);
    int deleteUserById(int id);
    int updateUser(User user);
    int addUser(User user);
}
