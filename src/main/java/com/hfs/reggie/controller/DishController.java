package com.hfs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfs.reggie.common.R;
import com.hfs.reggie.dto.DishDto;
import com.hfs.reggie.entity.Category;
import com.hfs.reggie.entity.Dish;
import com.hfs.reggie.entity.DishFlavor;
import com.hfs.reggie.service.CategoryService;
import com.hfs.reggie.service.DishFlavorService;
import com.hfs.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /*
     * 新增菜品
     *
     * */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /*
     * 菜品信息分页
     *
     * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //构造条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);

    }

    /*
     * 根据id修改菜品信息
     * */
    @GetMapping("/{id}")
    public R<DishDto> update(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /*
     *保存菜品修改信息
     *
     * */
    @PutMapping
    public R<String> saveUpdate(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /*
     * 改变菜品的出售信息
     *
     * */
    @PostMapping("/status/{i}")
    public R<String> change(@PathVariable int i, @RequestParam List<Long> ids) {
        log.info("i是{},{}", ids, i);
        for (Long ids1 : ids) {
            Dish byId = dishService.getById(ids1);
            byId.setStatus(i);
            dishService.updateById(byId);
        }
        return R.success("修改成功");


    }

    /*
     * 删除菜品信息
     * */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("{}", ids);
        for (Long ids1 : ids) {
            dishService.removeById(ids1);
        }
        return R.success("删除成功！");
    }
        /*
        * 查询菜品信息
        *
        * */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//        //添加查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        //查询状态是1
//        queryWrapper.eq(Dish::getStatus, 1);
//        // 添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }

        @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //添加查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //查询状态是1
        queryWrapper.eq(Dish::getStatus, 1);
        // 添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);


            List<DishDto> dishDtoList = list.stream().map((item) -> {
                DishDto dishDto = new DishDto();

                BeanUtils.copyProperties(item, dishDto);

                Long categoryId = item.getCategoryId();//分类id
                //根据id查询分类对象
                Category category = categoryService.getById(categoryId);

                if (category != null) {
                    String categoryName = category.getName();
                    dishDto.setCategoryName(categoryName);
                }
                Long dishId = item.getId();
                LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
                List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
                dishDto.setFlavors(dishFlavors);
                return dishDto;
            }).collect(Collectors.toList());

            return R.success(dishDtoList);
    }


}
