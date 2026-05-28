package com.campus.marketplace.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private String avatar;

    private String campus;

    private String realName;

    private Integer verifyStatus;

    private String verifyImage;

    private BigDecimal goodRate;

    private Integer totalReviews;

    private Integer goodReviews;

    private Integer role;

    private Integer status;

    private LocalDateTime lockUntil;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
