package com.gxx.springbootjedis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gxx.springbootjedis.mapper")
public class SpringbootJedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJedisApplication.class, args);
    }

}
