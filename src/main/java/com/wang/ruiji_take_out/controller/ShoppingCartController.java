package com.wang.ruiji_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wang.ruiji_take_out.common.Result;
import com.wang.ruiji_take_out.pojo.ShoppingCart;
import com.wang.ruiji_take_out.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        shoppingCart.setUserId(1L);

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());

        Long dishId = shoppingCart.getDishId();
        if (dishId!=null){
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart dbShoppingCart = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if (dbShoppingCart==null){
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            return Result.success(shoppingCart);
        }else {
            dbShoppingCart.setNumber(dbShoppingCart.getNumber()+1);
            shoppingCartService.updateById(dbShoppingCart);
            return Result.success(dbShoppingCart);
        }

    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        Long userId = 1L;
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId).orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result<String> clean(){
        Long userId = 1L;
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return Result.success("清空购物车成功");
    }

}
