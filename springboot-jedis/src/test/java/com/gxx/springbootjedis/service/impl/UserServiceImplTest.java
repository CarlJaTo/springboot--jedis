package com.gxx.springbootjedis.service.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import com.gxx.springbootjedis.entity.User;
import com.gxx.springbootjedis.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Test
    public void test(){
        System.out.println(userService.selectList());
    }
    @Test
    public void findUserById(){
        System.out.println(userService.findUserById(1007));
    }
    @Test
    public void addUser(){
        User user = new User();
        user.setUserName("gxx");
        user.setPassword("gxx");
        System.out.println(userService.addUser(user));
    }
    @Test
    public void updateUser(){
        User user = new User();
        user.setUserId(1007);
        user.setUserName("rood");
        user.setPassword("gxx");
        System.out.println(userService.updateUser(user));
    }
    @Test
    public void deleteUser(){
        System.out.println(userService.deleteUserById(1007));
    }
}