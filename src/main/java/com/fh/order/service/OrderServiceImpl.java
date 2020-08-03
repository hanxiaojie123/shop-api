package com.fh.order.service;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.member.po.Member;
import com.fh.order.mapper.OrderInfoMapper;
import com.fh.order.mapper.OrderMapper;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.BigDecimalUtil;
import com.fh.util.IdUtil;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ProductService productService;

    @Override
    public ServerResponse buildOrder(List<Cart> cartList, Integer payType, Integer addressId, Member member) {

        //根据雪花运算的到订单Id
        String orderId = IdUtil.createId();
        //订单详情
        List<OrderInfo> orderInfoList=new ArrayList<>();
        //总价格
        BigDecimal totalPrice = new BigDecimal("0.00");
        //库存不足的集合，提示用户会用到
        List<String> stockNotFull  = new ArrayList<>();

        for (Cart cart : cartList) {
            Product product = productService.selectProductById(cart.getProductId());
            if (product.getStock()<cart.getCount()){
                //库存不足
                stockNotFull.add(cart.getName());
            }
            else {

                Long stockId = productService.updateStock(product.getId(), cart.getCount());
                //stockId==1 可以修改库存就是存库充足
                if (stockId == 1) {
                    //存库充足，生成订单详情
                    OrderInfo orderInfo = buildOrderInfo(orderId, cart);
                    //订单详情添加到集合中
                    orderInfoList.add(orderInfo);
                    //调用价格工具类的mul乘法方法
                    BigDecimal subTotal = BigDecimalUtil.mul(cart.getPrice().toString(), cart.getCount() + "");
                    //调用价格工具类的mul加法方法计算总价格
                    totalPrice = BigDecimalUtil.add(totalPrice, subTotal);

                } else {
                    //库存不足
                    stockNotFull.add(cart.getName());
                }
            }
        }
        //生成订单 先判断是否有库存不足的商品
        if(orderInfoList!=null && orderInfoList.size()==cartList.size()){
            //条件满足，就是库存足够，保存订单详情
            for (OrderInfo orderInfo : orderInfoList) {
                //遍历保存订单详情
                orderInfoMapper.insert(orderInfo);
                //更新购物车商品信息
                updateRedisCart(member,orderInfo);
            }
            //生成订单
            buildOrder(payType,addressId,member,orderId,totalPrice);
            return ServerResponse.success(orderId);
        }
        return ServerResponse.error(stockNotFull);
    }


    //生成订单
    private void buildOrder(Integer payType, Integer addressId, Member member, String orderId, BigDecimal totalPrice) {
        Order order = new Order();
        order.setPayType(payType);
        order.setCreateDate(new Date());
        order.setAddressId(addressId);
        order.setStatus(SystemConstant.ORDER_STATUS_WAIT);
        order.setId(orderId);
        order.setTotalPrice(totalPrice);
        order.setMemberId(member.getId());
        orderMapper.insert(order);
    }

    //修改购物车商品
    private void updateRedisCart(Member member, OrderInfo orderInfo) {
        String cartJson = RedisUtil.hget(SystemConstant.CART_KEY + member.getId(), orderInfo.getProductId().toString());
        //判断获取订单的cartJson是否为null
        if (StringUtils.isNotEmpty(cartJson)){
            //不是null 将JSON转为对象
            Cart cart = JSONObject.parseObject(cartJson, Cart.class);
            //判断购物车中的数量小于提交订单的数量 就删除提交订单并删除
            if(cart.getCount()<=orderInfo.getCount()){
                //删除购物车 提交的商品
                RedisUtil.hdel(SystemConstant.CART_KEY+member.getId(),orderInfo.getProductId().toString());
            }else {
                //更新购物车
                cart.setCount(cart.getCount()-orderInfo.getCount());
                String cart1 = JSONObject.toJSONString(cart);
                RedisUtil.hset(SystemConstant.CART_KEY + member.getId(), orderInfo.getProductId().toString(),cart1);
            }
        }
    }

    //生成订单详情方法
    private OrderInfo buildOrderInfo(String orderId, Cart cart) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setName(cart.getName());
        orderInfo.setCount(cart.getCount());
        orderInfo.setPrice(cart.getPrice());
        orderInfo.setProductId(cart.getProductId());
        orderInfo.setFilePath(cart.getFilePath());
        orderInfo.setOrderId(orderId);
        return orderInfo;

    }

}
