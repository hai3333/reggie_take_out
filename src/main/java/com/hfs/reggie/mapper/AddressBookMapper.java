package com.hfs.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfs.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
