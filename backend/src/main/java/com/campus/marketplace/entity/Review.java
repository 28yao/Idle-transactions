package com.campus.marketplace.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("reviews")
public class Review {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long transactionId;

    private Long reviewerId;

    private Integer rating;

    private String content;

    private String tags;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
