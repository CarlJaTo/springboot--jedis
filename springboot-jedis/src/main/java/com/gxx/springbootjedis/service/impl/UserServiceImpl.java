package com.gxx.springbootjedis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gxx.springbootjedis.entity.User;
import com.gxx.springbootjedis.mapper.UserMapper;
import com.gxx.springbootjedis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<User> selectList() {
        /**
         * 1.先查redis中有没有用户列表的数据，如果有就直接显示redis中的数据
         * 2.如果没有就查Mysql中的用户数据，然后写入到redis中，并返回给控制器
         * （实际生产：会做缓存预热，预先查Mysql中的数据，存到redis中）
         */

        List<User> userKey = redisTemplate.opsForList().range("user_key", 0, -1);
        System.out.println("redis查到的----->："+userKey);
        if (userKey.size()==0){
            userKey = userMapper.selectList(null);
            System.out.println("数据库查到的----->:"+userKey);
            redisTemplate.opsForList().leftPushAll("user_key",userKey);
        }
        return userKey;
    }

    @Override
    public User findUserById(int id) {
        String key = "user_"+id;
        ValueOperations<String,User> operations = redisTemplate.opsForValue();
        //判断redis中是否有键为key的缓存
        boolean hasKey = redisTemplate.hasKey(key);
        if(hasKey){
            User user = operations.get(key);
            System.out.println("从缓存中获取数据:"+user);
            return user;
        }else{
            User user = userMapper.selectById(id);
            System.out.println("查询数据库获取数据:"+user.getUserName());
            operations.set(key,user,5, TimeUnit.HOURS);
            return user;
        }
    }
    @Override
    public int updateUser(User user) {
        ValueOperations<String,User> operations = redisTemplate.opsForValue();
        int result = userMapper.updateById(user);
        if(result!=0){
            String key = "user_"+user.getUserId();
            Boolean hasKey = redisTemplate.hasKey(key);
            if(hasKey){
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key："+key);
            }
            //再将更新后的数据加入缓存
            User userNew = userMapper.selectById(user.getUserId());
            if (userNew!=null){
                operations.set(key,userNew,3,TimeUnit.HOURS);
            }
        }
        return result;
    }
    @Override
    public int addUser(User user) {
        ValueOperations<String,User> operations = redisTemplate.opsForValue();
        int result = userMapper.insert(user);
        if(result!=0){
            String key = "user_"+user.getUserId();
            operations.set(key,user,3,TimeUnit.HOURS);
        }
        return result;
    }
    /**
     * 删除用户
     */
    @Override
    public int deleteUserById(int id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",id);
        int result = userMapper.delete(queryWrapper);
        String key = "user_"+id;
        if (result!=0){
            Boolean hasKey = redisTemplate.hasKey(key);
            if (hasKey){
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key："+key);
            }
        }
        return result;
    }

}
