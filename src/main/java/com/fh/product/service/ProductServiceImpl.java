package com.fh.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fh.common.ServerResponse;
import com.fh.product.mapper.ProductMapper;
import com.fh.product.model.Product;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public ServerResponse queryHostList() {
        QueryWrapper<Product> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("isHot",1);
        List<Product> list = productMapper.selectList(queryWrapper);
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse queryProductList() {
        List<Product> list = productMapper.selectList(null);
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse queryPageProduct(Long currentPage,Long pageSize) {
        Long start =(currentPage-1)*pageSize;
        //查询中条数
        Long totalCount=productMapper.queryProductCount();
        List<Product> list =productMapper.queryPageProduct(start,pageSize);

        Long  totalPage =totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
       Map mm=new HashMap<>();
        mm.put("list",list);
        mm.put("totalPage",totalPage);
        return ServerResponse.success(mm);
    }

    @Override
    public Product selectProductById(Integer productId) {
        Product product = productMapper.selectById(productId);
        return product;
    }

    @Override
    public Long updateStock(Integer id, int count) {
        return productMapper.updateStock(id,count);
    }

    @Override
    public ServerResponse isNotCart(Integer isNot) {
        return productMapper.isNotCart(isNot);
    }

}
