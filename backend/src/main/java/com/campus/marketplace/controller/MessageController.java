package com.campus.marketplace.controller;

import com.campus.marketplace.dto.request.MessageSendRequest;
import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.dto.response.MessageResponse;
import com.campus.marketplace.interceptor.AuthInterceptor;
import com.campus.marketplace.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ApiResponse<MessageResponse> send(@Valid @RequestBody MessageSendRequest request,
                                              HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(messageService.sendMessage(userId, request));
    }

    @GetMapping("/conversation/{conversationId}")
    public ApiResponse<List<MessageResponse>> list(@PathVariable("conversationId") Long conversationId,
                                                    @RequestParam(value = "limit", defaultValue = "50") int limit,
                                                    HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(messageService.getMessages(conversationId, userId, limit));
    }

    @PostMapping("/{id}/offer")
    public ApiResponse<MessageResponse> handleOffer(@PathVariable("id") Long id,
                                                     @RequestBody Map<String, Integer> body,
                                                     HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        Integer action = body.get("action");
        if (action == null || (action != 1 && action != 2)) {
            return ApiResponse.error(400, "action 必须为 1(接受) 或 2(拒绝)");
        }
        return ApiResponse.success(messageService.handleOffer(id, userId, action));
    }
}
