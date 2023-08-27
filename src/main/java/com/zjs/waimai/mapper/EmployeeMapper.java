package com.zjs.waimai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjs.waimai.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
 * 继承的BaseMapper中已经实现了增删改查等语句
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
}
