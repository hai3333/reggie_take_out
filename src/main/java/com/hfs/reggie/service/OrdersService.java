package com.hfs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hfs.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
