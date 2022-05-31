package com.hfs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hfs.reggie.common.CustomException;
import com.hfs.reggie.dto.SetmealDto;
import com.hfs.reggie.entity.Setmeal;
import com.hfs.reggie.entity.SetmealDish;
import com.hfs.reggie.mapper.SetmealMapper;
import com.hfs.reggie.service.SetmealDishService;
import com.hfs.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    /*
     * 新增菜品 保持套餐和菜品的关联关系
     *
     * */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);


    }
    /*
     * 删除套餐
     *
     * */

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐餐状态 确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        //不能删除抛出来业务异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中,不可以删除");
        }
        //可以删除的话先删除表里面的数据--setmeal_dish
        this.removeByIds(ids);

        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }

}
