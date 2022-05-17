package com.hfs.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfs.reggie.common.R;
import com.hfs.reggie.entity.Category;
import com.hfs.reggie.entity.Employee;
import com.hfs.reggie.service.CategoryService;
import com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/category")
@RestController
@Slf4j
public class CategoryController {
    @Autowired
    public CategoryService categoryService;

    /*
     * 添加菜品
     * */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category={}", category);

        categoryService.save(category);
        return R.success("新增加分类成功！");
    }
    /*
    * 展示菜品信息
    * */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //分页构造器
       Page pageInfo=new Page(page,pageSize);
        //构造条件创造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /*
    * 删除分类
    *
    * */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("{}",ids);
       categoryService.remove(ids);
       return R.success("删除成功");
    }

    /*
    * 修改菜品信息
    * */
    @PutMapping
    public  R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");

    }


}
