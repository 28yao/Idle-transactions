package com.campus.marketplace.controller;

import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.dto.response.PageResponse;
import com.campus.marketplace.dto.response.TransactionResponse;
import com.campus.marketplace.interceptor.AuthInterceptor;
import com.campus.marketplace.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ApiResponse<TransactionResponse> create(@RequestParam("productId") Long productId,
                                                    HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(transactionService.createTransaction(userId, productId));
    }

    @GetMapping("/{id}")
    public ApiResponse<TransactionResponse> detail(@PathVariable("id") Long id,
                                                    HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(transactionService.getTransactionDetail(id, userId));
    }

    @GetMapping
    public ApiResponse<PageResponse<TransactionResponse>> list(
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(transactionService.listTransactions(userId, status, page, size));
    }

    @PutMapping("/{id}/deliver")
    public ApiResponse<TransactionResponse> deliver(@PathVariable("id") Long id,
                                                     HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(transactionService.deliver(id, userId));
    }

    @PutMapping("/{id}/confirm")
    public ApiResponse<TransactionResponse> confirm(@PathVariable("id") Long id,
                                                     HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(transactionService.confirm(id, userId));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<TransactionResponse> requestCancel(@PathVariable("id") Long id,
                                                           @RequestParam(value = "reason", required = false) String reason,
                                                           HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(transactionService.requestCancel(id, userId, reason));
    }
}
