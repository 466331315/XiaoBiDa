package com.zjs.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjs.waimai.common.R;
import com.zjs.waimai.entity.Category;
import com.zjs.waimai.entity.Employee;
import com.zjs.waimai.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类保存成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageinfo= new Page<>(page,pageSize);
        //构造条件构造器,相当于数据库操作中的where name =......
        LambdaQueryWrapper<Category> queryWrapper =new LambdaQueryWrapper<>();
        //添加过滤条件
//        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件，按照实体中的sort方法进行排序
        queryWrapper.orderByAsc(Category::getSort);
        //执行查询
        categoryService.page(pageinfo,queryWrapper);
        return R.success(pageinfo);
    }
    @DeleteMapping
    public R<String> delete(@RequestParam Long ids){
        log.info("删除分类，id为{}",ids);
//        categoryService.removeById(id);
        categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }
    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);

        categoryService.updateById(category);

        return R.success("修改分类信息成功");
    }
    /**
     * 新增菜品管理,根据条件进行查询,用于回显下拉数据框中分类的回显
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper= new LambdaQueryWrapper<>();
        //添加条件,类型为Category 具体的值为category.getType()
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list=categoryService.list(queryWrapper);
        return R.success(list);
    }
}
