package com.hfs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfs.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
