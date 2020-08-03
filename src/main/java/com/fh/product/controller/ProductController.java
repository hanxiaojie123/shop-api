package com.fh.product.controller;

import com.fh.common.LogMsg;
import com.fh.common.ServerResponse;
import com.fh.product.service.ProductService;
import com.fh.util.annotation.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("product")
public class ProductController {

        @Autowired
        private ProductService productService;

        @RequestMapping("queryHostList")
        @Ignore
        public ServerResponse queryHostList(){
         return productService.queryHostList();
        }
        @RequestMapping("queryProductList")
        @Ignore
        public ServerResponse queryProductList(){
         return productService.queryProductList();
        }
        //分页查询商品信息
        @RequestMapping("queryPageProduct")
        @Ignore
        public ServerResponse queryPageProduct( Long currentPage,Long pageSize) {
        return productService.queryPageProduct(currentPage,pageSize);
        }
        //查询上架的商品
        @RequestMapping("isNotCart")
        public ServerResponse isNotCart(Integer isNot){
                return productService.isNotCart(isNot);
        }

}
