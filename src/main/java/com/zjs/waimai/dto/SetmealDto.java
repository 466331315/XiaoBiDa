package com.zjs.waimai.dto;


import com.zjs.waimai.common.R;
import com.zjs.waimai.entity.Setmeal;
import com.zjs.waimai.entity.SetmealDish;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(SetmealDto setmealDto){

        return null;
    }
}
