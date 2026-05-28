package com.campus.marketplace.dto.response;

import com.campus.marketplace.entity.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long id;
    private Long productId;
    private String productTitle;
    private String productImage;
    private Long buyerId;
    private String buyerNickname;
    private String buyerAvatar;
    private Long sellerId;
    private String sellerNickname;
    private String sellerAvatar;
    private BigDecimal price;
    private Integer status;
    private String cancelReason;
    private Long cancelBy;
    private LocalDateTime deliveredAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private Boolean reviewed;

    public static TransactionResponse fromEntity(Transaction t) {
        TransactionResponse r = new TransactionResponse();
        r.setId(t.getId());
        r.setProductId(t.getProductId());
        r.setBuyerId(t.getBuyerId());
        r.setSellerId(t.getSellerId());
        r.setPrice(t.getPrice());
        r.setStatus(t.getStatus());
        r.setCancelReason(t.getCancelReason());
        r.setCancelBy(t.getCancelBy());
        r.setDeliveredAt(t.getDeliveredAt());
        r.setCompletedAt(t.getCompletedAt());
        r.setCreatedAt(t.getCreatedAt());
        return r;
    }
}
