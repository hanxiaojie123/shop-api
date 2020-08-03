package com.fh.order.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
@Data
@TableName("t_orderinfo")
public class OrderInfo {
@TableId(type = IdType.INPUT)
    private  Integer id;
    private String orderId;
    private Integer productId;
    private  String filePath;
    private  String name;
    private Integer count;
    private BigDecimal price;

}
