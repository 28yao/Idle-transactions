package com.campus.marketplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.marketplace.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
