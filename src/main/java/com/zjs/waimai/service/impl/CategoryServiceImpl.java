package com.zjs.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjs.waimai.common.CustomException;
import com.zjs.waimai.entity.Category;
import com.zjs.waimai.entity.Dish;
import com.zjs.waimai.entity.Setmeal;
import com.zjs.waimai.mapper.CategoryMapper;
import com.zjs.waimai.service.CategoryService;
import com.zjs.waimai.service.DishService;
import com.zjs.waimai.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long ids) {
        /**
         * 根据id删除分类，删除之前需要进行判断
         */
        //查询分类是否关联了菜品，如果已经关联则抛出一个业务异常
        //select count(*) from Dish where Categoryid=?
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count= dishService.count(dishLambdaQueryWrapper);
        if (count>0){
            throw new CustomException("当前分类下关联了菜品，无法删除");
        }

        //查询分类是否关联了套餐，如果已经关联则抛出一个业务异常

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count1= setmealService.count(setmealLambdaQueryWrapper);
        if (count1>0){
            throw new CustomException("当前分类下关联了套餐，无法删除");
        }
        //正常删除
        super.removeById(ids);
    }
}
