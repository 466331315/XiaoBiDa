package com.zjs.waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjs.waimai.entity.DishFlavor;
import com.zjs.waimai.mapper.DishFlavorMapper;
import com.zjs.waimai.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
