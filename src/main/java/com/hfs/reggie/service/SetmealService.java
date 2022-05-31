package com.hfs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hfs.reggie.dto.SetmealDto;
import com.hfs.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /*
     * 新增菜品
     * */
    public void saveWithDish(SetmealDto setmealDto);

    /*
    * 删除套餐
    *
    * */
    public  void removeWithDish(List<Long> ids);
}
