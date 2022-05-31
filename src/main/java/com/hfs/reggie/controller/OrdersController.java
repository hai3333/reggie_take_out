package com.hfs.reggie.controller;


import com.hfs.reggie.common.R;
import com.hfs.reggie.entity.Orders;
import com.hfs.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
   private OrdersService ordersService;

    @RequestMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("{}",orders.toString());
        ordersService.submit(orders);
        return R.success("下单成功");

    }

}
