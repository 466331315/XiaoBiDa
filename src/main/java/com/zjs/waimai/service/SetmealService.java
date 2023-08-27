package com.zjs.waimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjs.waimai.dto.SetmealDto;
import com.zjs.waimai.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，需要同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     */
    public void removeWithDish(List<Long> ids);
}
