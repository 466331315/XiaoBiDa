package com.zjs.waimai.dto;

import com.zjs.waimai.entity.Dish;
import com.zjs.waimai.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
