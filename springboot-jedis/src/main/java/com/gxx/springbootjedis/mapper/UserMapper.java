package com.gxx.springbootjedis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxx.springbootjedis.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
