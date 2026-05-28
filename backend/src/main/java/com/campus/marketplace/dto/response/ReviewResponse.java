package com.campus.marketplace.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {

    private Long id;
    private Long transactionId;
    private Long reviewerId;
    private String reviewerNickname;
    private String reviewerAvatar;
    private Long targetId;
    private String targetNickname;
    private Integer rating;
    private String content;
    private String tags;
    private LocalDateTime createdAt;
}
