package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    public void  saveWithDish(SetmealDto setmealDto);
    public void  removeWithDish(Long[] ids);

    public SetmealDto getWithDish(Long id);

    public void updateWithDish(SetmealDto setmealDto);
}
