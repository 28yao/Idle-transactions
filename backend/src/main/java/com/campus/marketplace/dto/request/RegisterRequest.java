package com.campus.marketplace.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "请输入正确的邮箱格式")
    private String email;

    @NotBlank(message = "昵称不能为空")
    @Size(min = 2, max = 20, message = "昵称长度为2-20位")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度为8-20位")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "密码必须包含字母和数字")
    private String password;

    @NotBlank(message = "校区不能为空")
    private String campus;
}
