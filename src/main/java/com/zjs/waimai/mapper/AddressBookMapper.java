package com.zjs.waimai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjs.waimai.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
