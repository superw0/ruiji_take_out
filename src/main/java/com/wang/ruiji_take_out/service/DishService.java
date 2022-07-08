package com.wang.ruiji_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.ruiji_take_out.dto.DishDto;
import com.wang.ruiji_take_out.pojo.Dish;


public interface DishService extends IService<Dish> {

    public void save(DishDto dishDto);

    public DishDto getDishDto(Long id);

    public void updateDishWithFlavors(DishDto dishDto);
}
