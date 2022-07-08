package com.wang.ruiji_take_out.dto;

import com.wang.ruiji_take_out.pojo.Setmeal;
import com.wang.ruiji_take_out.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
