package com.fh.order.controller;

import com.fh.common.LogMsg;
import com.fh.common.ServerResponse;
import com.fh.order.service.orderInfo.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("orderInfo")
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;

}
