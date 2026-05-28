package com.campus.marketplace.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("products")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long sellerId;

    private String title;

    private String description;

    private String category;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String condition;

    private String location;

    private String campus;

    private Integer status;

    private Integer viewCount;

    private Integer favCount;

    private String rejectReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
