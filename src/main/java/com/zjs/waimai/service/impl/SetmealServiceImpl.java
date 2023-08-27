package com.zjs.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjs.waimai.common.CustomException;
import com.zjs.waimai.dto.SetmealDto;
import com.zjs.waimai.entity.Setmeal;
import com.zjs.waimai.entity.SetmealDish;
import com.zjs.waimai.mapper.SetmealMapper;
import com.zjs.waimai.service.SetmealDishService;
import com.zjs.waimai.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，需要同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal表，执行insert操作
        this.save(setmealDto);
        List<SetmealDish> setmealDishes =setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息，操作setmeal_dish表，执行insert操作
        setmealDishService.saveBatch(setmealDishes);


    }
    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        //select count(*) from setmeal where id=... and status=1
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count=this.count(queryWrapper);
        //如果不能删除，抛出一个业务异常-setmeal
        if (count>0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }


        //可以删除，先删除套餐表的数据setmeal_dish
        this.removeByIds(ids);
        //删除关系表的数据
        //delete from setmeal_dish where setmwal_id in ....
        LambdaQueryWrapper<SetmealDish> queryWrapper1=new LambdaQueryWrapper();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);


    }
}
