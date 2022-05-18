package com.hfs.reggie.dto;


import com.hfs.reggie.entity.Setmeal;
import com.hfs.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;


}
