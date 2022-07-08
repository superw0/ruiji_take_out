package com.wang.ruiji_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.ruiji_take_out.common.Result;
import com.wang.ruiji_take_out.dto.DishDto;
import com.wang.ruiji_take_out.pojo.Category;
import com.wang.ruiji_take_out.pojo.Dish;
import com.wang.ruiji_take_out.pojo.DishFlavor;
import com.wang.ruiji_take_out.service.CategoryService;
import com.wang.ruiji_take_out.service.DishFlavorService;
import com.wang.ruiji_take_out.service.DishService;
import kotlin.jvm.internal.Lambda;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {


    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public  Result<String> save(@RequestBody DishDto dishDto){

        dishService.save(dishDto);
        return Result.success("新增菜品成功！");

    }

    @GetMapping("/page")
    public  Result<Page> page(Integer page,Integer pageSize,String name){

        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> newPageInfo=new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name);
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,dishLambdaQueryWrapper);

        BeanUtils.copyProperties(pageInfo,newPageInfo,"records");

        List<Dish> records = pageInfo.getRecords();
            List<DishDto> newRecords = records.stream().map(record -> {
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(record, dishDto);

                Long categoryId = record.getCategoryId();
                Category category = categoryService.getById(categoryId);
                dishDto.setCategoryName(category.getName());

                return dishDto;
            }).collect(Collectors.toList());

        newPageInfo.setRecords(newRecords);

        return Result.success(newPageInfo);

    }

    @GetMapping("/{id}")
    public Result<DishDto> getDishDto(@PathVariable Long id){

        DishDto dishDto = dishService.getDishDto(id);
        return Result.success(dishDto);
    }


    @PutMapping
    public  Result<String> updateDishWithFlavors(@RequestBody DishDto dishDto){

        dishService.updateDishWithFlavors(dishDto);
        return Result.success("修改菜品成功！");

    }

    /*@GetMapping("/list")
    public Result<List<Dish>> listDishes(Dish dish){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId()).eq(Dish::getStatus,1);
        List<Dish> dishes = dishService.list(dishLambdaQueryWrapper);
        return Result.success(dishes);

    }*/

    @GetMapping("/list")
    public Result<List<DishDto>> list(Dish dish) {
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(dish.getName()), Dish::getName, dish.getName());
        queryWrapper.eq(null != dish.getCategoryId(), Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        List<Dish> dishs = dishService.list(queryWrapper);

        List<DishDto> dishDtos = dishs.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, item.getId());

            dishDto.setFlavors(dishFlavorService.list(wrapper));
            return dishDto;
        }).collect(Collectors.toList());

        return Result.success(dishDtos);
    }
}
