package com.hfs.reggie.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    UserServiceImpl userService;
    @Test
    public void test(){
        System.out.println("******测试");
        int count = userService.count();
        System.out.println(count);
        log.info("{}",count);

    }
}