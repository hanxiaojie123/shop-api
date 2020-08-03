package com.fh.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.Idempotent;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.member.po.Member;
import com.fh.order.service.OrderService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("buildOrder")
    @Idempotent
    public ServerResponse buildOrder(String listStr, Integer payType, Integer addressId, HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        //判断购物车是否有商品
        List<Cart> cartList=new ArrayList<>();
        if (StringUtils.isNotEmpty(listStr)){
           cartList = JSONObject.parseArray(listStr, Cart.class);
        }else{
                return ServerResponse.error("请选择商品");
        }
        return orderService.buildOrder(cartList,payType,addressId,member);
    }

    @RequestMapping("getToken")
    public ServerResponse getToken(){
        String mtoken = UUID.randomUUID().toString();
        RedisUtil.set(mtoken,mtoken);
        return ServerResponse.success(mtoken);
    }

    //验证库存是否充足

}
