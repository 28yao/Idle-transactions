package com.campus.marketplace.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ConversationResponse {

    private Long id;
    private Long productId;
    private String productTitle;
    private String productCover;
    private BigDecimal productPrice;

    private Long otherUserId;
    private String otherNickname;
    private String otherAvatar;

    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private Integer unreadCount;

    private LocalDateTime createdAt;
}
