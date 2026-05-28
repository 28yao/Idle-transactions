package com.campus.marketplace.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MessageResponse {

    private Long id;
    private Long conversationId;
    private Long senderId;
    private String senderNickname;
    private String senderAvatar;

    private String content;
    private Integer type; // 1文字 2图片 3出价 4还价
    private BigDecimal priceOffer;
    private Integer offerStatus; // 0待处理 1接受 2拒绝

    private LocalDateTime createdAt;
}
