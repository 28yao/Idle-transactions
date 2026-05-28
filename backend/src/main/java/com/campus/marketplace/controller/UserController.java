package com.campus.marketplace.controller;

import com.campus.marketplace.dto.request.VerifyRequest;
import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.interceptor.AuthInterceptor;
import com.campus.marketplace.service.FileService;
import com.campus.marketplace.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    @PostMapping("/verify/image")
    public ApiResponse<Map<String, String>> uploadVerifyImage(@RequestParam("file") MultipartFile file) {
        String url = fileService.uploadImage(file, "verify");
        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        return ApiResponse.success(data);
    }

    @PostMapping("/verify")
    public ApiResponse<Void> submitVerify(@Valid @RequestBody VerifyRequest request,
                                          HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        userService.submitVerify(userId, request);
        return ApiResponse.success("提交成功，等待审核", null);
    }
}
