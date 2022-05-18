package com.hfs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hfs.reggie.dto.DishDto;
import com.hfs.reggie.entity.Dish;
import org.springframework.stereotype.Service;


public interface DishService extends IService<Dish> {

    //新增菜品同时插入对应的口味数据 操作两张表
    public void saveWithFlavor(DishDto dishDto);

    public  DishDto getByIdWithFlavor(Long id);
    public void updateWithFlavor(DishDto dishDto);
}
