package com.gxx.springbootjedis.controller;

import com.gxx.springbootjedis.entity.User;
import com.gxx.springbootjedis.utils.JedisUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RegisterController {

    @RequestMapping("/add")
    public String add() {
        return "add";
    }

    @RequestMapping("/doAdd")
    public String doAdd(User user) {
        //通过JedisUtils获取jedis连接池对象
        Jedis jedis = JedisUtils.getJedis();
        //redis密码
        /*jedis.auth("gxx");*/
        //存取数据
        jedis.sadd("user", "user-" + user.getUserId());
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(user.getUserId()));
        map.put("name", user.getUserName());
        map.put("name", user.getPassword());
        jedis.hmset("user-" + user.getUserId(), map);
        return "redirect:/";
    }

    @RequestMapping("/")
    public String index(Model model) {
        List<User> userList = new ArrayList<User>();
        //通过JedisUtils获取jedis连接池对象
        Jedis jedis = JedisUtils.getJedis();
        //redis密码
        /*jedis.auth("gxx");*/
        User user = null;
        if (jedis.exists("user")) {
            for (String str : jedis.smembers("user")) {
                user = new User();
                List<String> list = jedis.hmget(str, "userId", "userName", "password");
                user.setUserId(Integer.valueOf(list.get(0)));
                user.setUserName(list.get(1));
                user.setPassword(list.get(4));
                userList.add(user);
            }
        }
        model.addAttribute("userList",userList);
        System.out.println(userList);
        return "index";
    }

    @RequestMapping("/del")
    public String del(Integer id) {
        //通过JedisUtils获取jedis连接池对象
        Jedis jedis = JedisUtils.getJedis();
        //redis密码
        /*jedis.auth("gxx");*/
        if (jedis.exists("user")) {
            jedis.srem("user","user-"+id);
        }
        return "redirect:/";
    }

    @RequestMapping("/update")
    public String update(User user,Model model) {
        model.addAttribute("user",user);
        return "update";
    }

    @RequestMapping("/doUpdate")
    public String doUpdate(User user) {
        //通过JedisUtils获取jedis连接池对象
        Jedis jedis = JedisUtils.getJedis();
        //redis密码
        /*jedis.auth("gxx");*/
        jedis.sadd("user", "user-" + user.getUserId());
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(user.getUserId()));
        map.put("name", user.getUserName());
        map.put("phone", user.getPassword());
        jedis.hmset("user-" + user.getUserId(), map);
        return "redirect:/";
    }

}
