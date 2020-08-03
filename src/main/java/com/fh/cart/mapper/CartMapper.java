package com.fh.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.cart.model.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CartMapper extends BaseMapper<Cart> {
}
