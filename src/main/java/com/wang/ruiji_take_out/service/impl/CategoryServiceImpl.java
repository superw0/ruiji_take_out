package com.wang.ruiji_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.ruiji_take_out.common.CostomException;
import com.wang.ruiji_take_out.mapper.CategoryMapper;
import com.wang.ruiji_take_out.pojo.Category;
import com.wang.ruiji_take_out.pojo.Dish;
import com.wang.ruiji_take_out.pojo.Setmeal;
import com.wang.ruiji_take_out.service.CategoryService;
import com.wang.ruiji_take_out.service.DishService;
import com.wang.ruiji_take_out.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQuery=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setmealQuery=new LambdaQueryWrapper<>();

        dishQuery.eq(Dish::getCategoryId,id);
        int count1 = (int) dishService.count(dishQuery);
        if (count1>0){
            throw new CostomException("当前分类下关联了菜品，删除失败！");
        }
        setmealQuery.eq(Setmeal::getCategoryId,id);
        int count2 = (int) setmealService.count(setmealQuery);
        if (count2>0){
            throw new CostomException("当前分类下关联了套餐，删除失败！");
        }
        super.removeById(id);
    }
}
