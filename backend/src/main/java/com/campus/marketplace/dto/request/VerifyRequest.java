package com.campus.marketplace.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyRequest {

    @NotBlank(message = "请输入真实姓名")
    private String realName;

    @NotBlank(message = "请上传认证图片")
    private String verifyImage;
}
