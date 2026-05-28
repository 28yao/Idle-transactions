package com.campus.marketplace.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "请输入正确的邮箱格式")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度为8-20位")
    private String password;
}
