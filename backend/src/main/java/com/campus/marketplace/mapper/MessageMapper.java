package com.campus.marketplace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.marketplace.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
