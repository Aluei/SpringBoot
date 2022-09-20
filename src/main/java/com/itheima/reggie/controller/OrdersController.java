package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据:{}",orders);
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){

        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> pageDtoInfo = new Page<>();
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, currentId);
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        Page<Orders> ordersPage = ordersService.page(pageInfo, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");
        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> ordersDtos = records.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            Orders orders = ordersService.getById(item.getId());
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrderDetail::getOrderId, orders.getId());
            List<OrderDetail> list = orderDetailService.list(lambdaQueryWrapper);

            ordersDto.setOrderDetails(list);

            int num = 0;
            for (OrderDetail l : list) {
                num += l.getNumber().intValue();
            }
            ordersDto.setNumber(String.valueOf(num));
            return ordersDto;
        }).collect(Collectors.toList());
        pageDtoInfo.setRecords(ordersDtos);

        return R.success(pageDtoInfo);
    }


    @GetMapping("/page")
    public R<Page> backendPage(int page, int pageSize){

        Page<Orders> pageInfo = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        ordersService.page(pageInfo, queryWrapper);
        List<Orders> records = pageInfo.getRecords();
        records.stream().map( item -> {
            item.setUserName(String.valueOf(item.getId()));
            return item;
        }).collect(Collectors.toList());
        pageInfo.setRecords(records);
        return R.success(pageInfo);
    }
}
