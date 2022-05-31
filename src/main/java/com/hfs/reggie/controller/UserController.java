package com.hfs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hfs.reggie.common.R;
import com.hfs.reggie.entity.User;
import com.hfs.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public R<User> login(@RequestBody User user ,HttpSession session ){
        //获取手机号 若是新用户直接注册

        String phone=user.getPhone();
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<User>();
        queryWrapper.eq(User::getPhone,phone);
        User user1 = userService.getOne(queryWrapper);
        if(user1==null){
            user1=new User();
            user1.setPhone(phone);
            user1.setStatus(1);
            userService.save(user1);

        }
        session.setAttribute("user",user1.getId());
        return R.success(user1);
    }



}
