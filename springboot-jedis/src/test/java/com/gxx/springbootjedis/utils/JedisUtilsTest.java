package com.gxx.springbootjedis.utils;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

public class JedisUtilsTest {

    @Test
    public void test(){
        //通过JedisUtils获取jedis连接池对象
        Jedis jedis = JedisUtils.getJedis();

        //redis密码
        /*jedis.auth("gxx");*/

        //存取数据
        jedis.set("userName","gxx");
        System.out.println(jedis.get("userName"));

        //关闭（将redis对象还给连接池）
        JedisUtils.closeJedis(jedis);
    }
}