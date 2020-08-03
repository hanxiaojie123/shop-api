package com.fh.cart.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.cart.service.CartService;
import com.fh.common.LogMsg;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.member.po.Member;
import com.fh.util.RedisUtil;
import com.fh.util.annotation.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("cart")
public class CarController {
    @Autowired
    private CartService cartService;

    @RequestMapping("buy")
    public ServerResponse buy(Integer productId, Integer count, HttpServletRequest request){
        return cartService.buy(productId,count,request);
    }
    @RequestMapping("queryCarProductCount")
    @Ignore
    public ServerResponse queryCarProductCount(HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        List<String> stringList = RedisUtil.hget(SystemConstant.CART_KEY + member.getId());
      long totalCount=0;
       if (stringList!=null && stringList.size()>0){
           for (String str : stringList) {
               Cart cart = JSONObject.parseObject(str, Cart.class);
               totalCount+=cart.getCount();
           }
       }else {
           return ServerResponse.success(0);
       }
        return ServerResponse.success(totalCount);
    }

    @RequestMapping("queryList")
    public ServerResponse queryList(HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        List<String> stringList = RedisUtil.hget(SystemConstant.CART_KEY + member.getId());
        List<Cart> cartList=new ArrayList<>();
       if (stringList!=null && stringList.size()>0){
           for (String str : stringList) {
               Cart cart = JSONObject.parseObject(str, Cart.class);
               cartList.add(cart);
           }
       }else {
           return ServerResponse.error(ServerEnum.CART_IS_NULL.getMsg());
       }
        return ServerResponse.success(cartList);
    }

    @RequestMapping("deleteCart/{productId}")
    public ServerResponse deleteCart(HttpServletRequest request,@PathVariable("productId") Integer productId) {
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        RedisUtil.hdel(SystemConstant.CART_KEY+member.getId(),productId.toString());
        return ServerResponse.success();
    }
    @RequestMapping("deleteCartBath")
    @LogMsg("删除商品")
    public ServerResponse deleteBatch(@RequestParam("idList") List<Integer> idList, HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        RedisUtil.hdel(SystemConstant.CART_KEY+member.getId(),idList.toString());
        return ServerResponse.success();

    }
    @RequestMapping("queryCart")
    public  ServerResponse queryCart(){
        return cartService.queryCart();
    }
}
