package com.fh.cart.service;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.mapper.CartMapper;
import com.fh.cart.model.Cart;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.member.po.Member;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductService productService;
    @Override
    public ServerResponse buy(Integer productId, Integer count, HttpServletRequest request) {
        //验证商品是否存在
        Product product = productService.selectProductById(productId);
        if (product == null) {
            return ServerResponse.error(ServerEnum.PRUDUCT_NOT_EXIST);
        }
        //验证商品是否上架
        if (product.getStatus() == 0){
            return ServerResponse.error(ServerEnum.PRUDUCT_IS_DUWN);
        }
        //验证购物车中是否有该商品
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        boolean exist = RedisUtil.exist(SystemConstant.CART_KEY+member.getId(), productId.toString());
        if (!exist){
            Cart cart = new Cart();
            cart.setProductId(productId);
            cart.setCount(count);
            cart.setName(product.getName());
            cart.setPrice(product.getPrice());
            cart.setFilePath(product.getFilePath());
            String jsonString = JSONObject.toJSONString(cart);
            RedisUtil.hset(SystemConstant.CART_KEY+member.getId(),productId.toString(),jsonString);
        }else {
            //如果存在则修改数量
            String productJson = RedisUtil.hget(SystemConstant.CART_KEY + member.getId(), productId.toString());
            Cart cart = JSONObject.parseObject(productJson, Cart.class);
            cart.setCount(cart.getCount()+count);
            //修改完之后再存入redis中
            String jsonString = JSONObject.toJSONString(cart);
            RedisUtil.hset(SystemConstant.CART_KEY+member.getId(),productId.toString(),jsonString);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse queryCart() {
        List<Cart> list = cartMapper.selectList(null);
        return ServerResponse.success(list);
    }



}
