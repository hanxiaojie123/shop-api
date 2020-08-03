package com.fh.cart.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Cart {
    private  Integer productId;
    private  int count;
    private String name;
    private String filePath;
    private BigDecimal price;
}
