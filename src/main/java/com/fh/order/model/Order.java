package com.fh.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
public class Order {
    @TableId(type = IdType.INPUT)
    private  String id;
    private Integer memberId;
    private Integer addressId;
    private Integer payType;
    private Integer status;
    private BigDecimal totalPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @TableField(exist = false)
     private List<OrderInfo> orderInfoList=new ArrayList<>();

}
