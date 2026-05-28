package com.campus.marketplace.dto.response;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String email;
    private String nickname;
    private String avatar;
    private String campus;
    private Integer role;
    private Integer verifyStatus;
    private String token;

    public static UserResponse fromEntity(com.campus.marketplace.entity.User user, String token) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setCampus(user.getCampus());
        response.setRole(user.getRole());
        response.setVerifyStatus(user.getVerifyStatus());
        response.setToken(token);
        return response;
    }

    public static UserResponse fromEntity(com.campus.marketplace.entity.User user) {
        return fromEntity(user, null);
    }
}
