package com.fh.product.service;

import com.fh.common.ServerResponse;
import com.fh.product.model.Product;

import java.util.List;

public interface ProductService {
    ServerResponse queryHostList();

    ServerResponse queryProductList();

    ServerResponse queryPageProduct(Long currentPage,Long pageSize);

    Product selectProductById(Integer productId);

    Long updateStock(Integer id, int count);

    ServerResponse isNotCart(Integer isNot);
}
