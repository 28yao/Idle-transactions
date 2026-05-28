package com.campus.marketplace.controller;

import com.campus.marketplace.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("服务运行正常");
    }
}
