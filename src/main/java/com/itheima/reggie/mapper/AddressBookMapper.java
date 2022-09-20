package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * 1. @ClassDescription:
 * 2. @author: ZhangL
 * 3. @date: 2022年09月16日 21:28
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
