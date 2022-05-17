package com.hfs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hfs.reggie.common.CustomException;
import com.hfs.reggie.entity.Category;
import com.hfs.reggie.entity.Dish;
import com.hfs.reggie.entity.Setmeal;
import com.hfs.reggie.mapper.CategoryMapper;
import com.hfs.reggie.service.CategoryService;
import com.hfs.reggie.service.DishService;
import com.hfs.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
   @Autowired
   private DishService dishService;
   @Autowired
   private SetmealService setmealService;


    /*
    * 根据id进行删除查询菜品是否关联了套餐
    *
    * */

    @Override
    public void remove(Long ids) {

     /*   //当前分类关联菜品的话抛出业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
       //根据查询条件进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count=dishService.count(dishLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("当前分类下关联了菜品 不可以删除！");

        }
        //当前分类关联套餐的话抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count2=setmealService.count();
        if(count2>0){
            throw new CustomException("当前菜品关联了套餐 不可以删除");
        }*/

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if(count1 > 0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(ids);


    }
}
