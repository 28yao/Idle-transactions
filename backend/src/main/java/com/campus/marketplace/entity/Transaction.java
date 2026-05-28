package com.campus.marketplace.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("transactions")
public class Transaction {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private Long buyerId;

    private Long sellerId;

    private BigDecimal price;

    private Integer status;

    private String cancelReason;

    private Long cancelBy;

    private LocalDateTime deliveredAt;

    private LocalDateTime completedAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
