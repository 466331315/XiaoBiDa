package com.zjs.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjs.waimai.common.CustomException;
import com.zjs.waimai.common.R;
import com.zjs.waimai.dto.DishDto;
import com.zjs.waimai.entity.Dish;
import com.zjs.waimai.entity.DishFlavor;
import com.zjs.waimai.entity.Setmeal;
import com.zjs.waimai.mapper.DishMapper;
import com.zjs.waimai.service.DishFlavorService;
import com.zjs.waimai.service.DishService;
import com.zjs.waimai.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional
    public void saveWithDlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        //保存菜品口味数据的基本信息到菜品表dish_flavor
        //由于添加菜品时出传入的dishID=null，因此需要对ID进行处理
        Long dishId=dishDto.getId();
        List<DishFlavor> flavors=dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
                }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
    //根据id查询菜品信息和对应的口味信息
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish=this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list=dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    public DishDto updateWithDlavor(DishDto dishDto) {
        //更新dish表的基本信息
        this.updateById(dishDto);
        //清理当前菜品对应的口味数据-dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据-dish_flavor表的insert操作
        List<DishFlavor> flavors=dishDto.getFlavors();
        //保存菜品口味数据的基本信息到菜品表dish_flavor
        //由于添加菜品时出传入的dishID=null，因此需要对ID进行处理
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);



        return dishDto;
    }
}
