package com.campus.marketplace.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("reports")
public class Report {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long reporterId;

    private Integer targetType;

    private Long targetId;

    private Integer type;

    private String reason;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
