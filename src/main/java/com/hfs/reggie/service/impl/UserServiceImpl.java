package com.hfs.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hfs.reggie.entity.User;
import com.hfs.reggie.mapper.UserMapper;
import com.hfs.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {
}
