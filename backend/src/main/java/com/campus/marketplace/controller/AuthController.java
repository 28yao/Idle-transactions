package com.campus.marketplace.controller;

import com.campus.marketplace.dto.request.LoginRequest;
import com.campus.marketplace.dto.request.RegisterRequest;
import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.dto.response.UserResponse;
import com.campus.marketplace.interceptor.AuthInterceptor;
import com.campus.marketplace.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = authService.register(request);
        return ApiResponse.success("注册成功", userResponse);
    }

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        UserResponse userResponse = authService.login(request);
        return ApiResponse.success("登录成功", userResponse);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.success("退出登录成功", null);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        UserResponse userResponse = authService.getUserById(userId);
        return ApiResponse.success(userResponse);
    }
}
