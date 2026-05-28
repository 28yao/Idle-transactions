package com.campus.marketplace.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("conversations")
public class Conversation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long user1Id;

    private Long user2Id;

    private Long productId;

    private String lastMessage;

    private LocalDateTime lastMessageAt;

    private Integer unreadCount1;

    private Integer unreadCount2;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
