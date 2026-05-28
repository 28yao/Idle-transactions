package com.campus.marketplace.controller;

import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.dto.response.ConversationResponse;
import com.campus.marketplace.interceptor.AuthInterceptor;
import com.campus.marketplace.service.ConversationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ApiResponse<ConversationResponse> create(@RequestParam("productId") Long productId,
                                                     HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(conversationService.createOrGetConversation(userId, productId));
    }

    @GetMapping
    public ApiResponse<List<ConversationResponse>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(conversationService.listConversations(userId));
    }

    @GetMapping("/{id}")
    public ApiResponse<ConversationResponse> detail(@PathVariable("id") Long id,
                                                      HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(conversationService.getConversation(id, userId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id,
                                     HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        conversationService.deleteConversation(id, userId);
        return ApiResponse.success("删除成功", null);
    }

    @PutMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(@PathVariable("id") Long id,
                                         HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        conversationService.markAsRead(id, userId);
        return ApiResponse.success(null);
    }
}
