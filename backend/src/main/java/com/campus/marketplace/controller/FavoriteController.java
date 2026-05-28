package com.campus.marketplace.controller;

import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.interceptor.AuthInterceptor;
import com.campus.marketplace.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{productId}")
    public ApiResponse<Void> add(@PathVariable("productId") Long productId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        favoriteService.addFavorite(userId, productId);
        return ApiResponse.success("收藏成功", null);
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> remove(@PathVariable("productId") Long productId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        favoriteService.removeFavorite(userId, productId);
        return ApiResponse.success("取消收藏成功", null);
    }

    @GetMapping("/check/{productId}")
    public ApiResponse<Map<String, Boolean>> check(@PathVariable("productId") Long productId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        Map<String, Boolean> data = new HashMap<>();
        data.put("favorited", favoriteService.isFavorited(userId, productId));
        return ApiResponse.success(data);
    }
}
