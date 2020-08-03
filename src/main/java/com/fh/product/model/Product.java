package com.fh.product.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class Product {

    private Integer id;
    private String name;
    private Integer  brandId;
    private BigDecimal price;
    private Integer  status;
    private String  filePath;
    private Integer  isHot;
    private Integer  stock;
    private Integer  categoryId1;
    private Integer  categoryId2;
    private Integer  categoryId3;

}
