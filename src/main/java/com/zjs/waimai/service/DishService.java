package com.zjs.waimai.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjs.waimai.dto.DishDto;
import com.zjs.waimai.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
    public void saveWithDlavor(DishDto dishDto);
    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    public DishDto updateWithDlavor(DishDto dishDto);
}
