package com.campus.marketplace.dto.response;

import com.campus.marketplace.entity.Review;
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

    public static ReviewResponse fromEntity(Review r) {
        ReviewResponse resp = new ReviewResponse();
        resp.setId(r.getId());
        resp.setTransactionId(r.getTransactionId());
        resp.setReviewerId(r.getReviewerId());
        resp.setTargetId(r.getTargetId());
        resp.setRating(r.getRating());
        resp.setContent(r.getContent());
        resp.setTags(r.getTags());
        resp.setCreatedAt(r.getCreatedAt());
        return resp;
    }
}
