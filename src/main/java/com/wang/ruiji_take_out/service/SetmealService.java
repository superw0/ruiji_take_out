package com.wang.ruiji_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.ruiji_take_out.dto.SetmealDto;
import com.wang.ruiji_take_out.pojo.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    public void saveWithDishes(SetmealDto setmealDto);

    public void deleteWithDishes(Long[] ids);
}
