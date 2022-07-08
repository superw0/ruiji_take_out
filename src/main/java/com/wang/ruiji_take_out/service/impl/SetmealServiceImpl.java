package com.wang.ruiji_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.ruiji_take_out.common.CostomException;
import com.wang.ruiji_take_out.dto.SetmealDto;
import com.wang.ruiji_take_out.mapper.SetmealMapper;
import com.wang.ruiji_take_out.pojo.Setmeal;
import com.wang.ruiji_take_out.pojo.SetmealDish;
import com.wang.ruiji_take_out.service.SetmealDishService;
import com.wang.ruiji_take_out.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDishes(SetmealDto setmealDto) {
        super.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long setmealId = setmealDto.getId();

        setmealDishes.stream().map(setmealDish->{
            setmealDish.setSetmealId(setmealId);
            return setmealDish;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    public void deleteWithDishes(Long[] ids) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids).eq(Setmeal::getStatus,1);
        long count = super.count(setmealLambdaQueryWrapper);
        if (count>1){
            throw new CostomException("套餐正在售卖中，无法删除");
        }
        super.removeByIds(Arrays.asList(ids));


        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);



    }
}
