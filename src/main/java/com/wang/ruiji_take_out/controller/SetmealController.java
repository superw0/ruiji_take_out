package com.wang.ruiji_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.ruiji_take_out.common.Result;
import com.wang.ruiji_take_out.dto.SetmealDto;
import com.wang.ruiji_take_out.pojo.Category;
import com.wang.ruiji_take_out.pojo.Setmeal;
import com.wang.ruiji_take_out.service.CategoryService;
import com.wang.ruiji_take_out.service.SetmealDishService;
import com.wang.ruiji_take_out.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {


    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveWithDishes(setmealDto);
        return Result.success("新增成功！");

    }

    @GetMapping("/page")
    public Result<Page<SetmealDto>> page(Integer page,Integer pageSize,String name){
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        Page<SetmealDto> newPageInfo=new Page<>(page,pageSize);
        BeanUtils.copyProperties(pageInfo,newPageInfo,"records");

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name!=null,Setmeal::getName,name)
                .orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,setmealLambdaQueryWrapper);

        List<Setmeal> setmeals = pageInfo.getRecords();
        List<SetmealDto> setmealDtos = setmeals.stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);

            Long categoryId = setmeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;

        }).collect(Collectors.toList());

        newPageInfo.setRecords(setmealDtos);

        return Result.success(newPageInfo);
    }

    @DeleteMapping
    public Result<String> deleteWithDishes(@RequestParam Long[] ids){
        setmealService.deleteWithDishes(ids);
        return Result.success("删除套餐成功!");
    }

}
