package com.zjs.waimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjs.waimai.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long ids);
}
