package com.campus.marketplace.dto.response;

import com.campus.marketplace.entity.User;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UserProfileResponse {

    private Long id;
    private String nickname;
    private String avatar;
    private String campus;
    private Integer verifyStatus;
    private Integer publishCount;
    private Integer transactionCount;
    private BigDecimal goodRate;
    private List<ReviewResponse> recentReviews;

    public static UserProfileResponse fromEntity(User user) {
        UserProfileResponse r = new UserProfileResponse();
        r.setId(user.getId());
        r.setNickname(user.getNickname());
        r.setAvatar(user.getAvatar());
        r.setCampus(user.getCampus());
        r.setVerifyStatus(user.getVerifyStatus());
        r.setPublishCount(0);
        r.setTransactionCount(0);
        r.setGoodRate(user.getGoodRate());
        return r;
    }
}
