package com.campus.marketplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.marketplace.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {
}
