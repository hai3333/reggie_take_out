package com.hfs.reggie.mapper;

import com.hfs.reggie.entity.User;
import com.hfs.reggie.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
@Slf4j
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;
    @Test
    public void test(){
        System.out.println("******测试");
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
        log.info("{}",users);

    }

}