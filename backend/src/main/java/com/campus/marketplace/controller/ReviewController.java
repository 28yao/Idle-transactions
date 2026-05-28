package com.campus.marketplace.controller;

import com.campus.marketplace.dto.request.ReviewRequest;
import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.dto.response.ReviewResponse;
import com.campus.marketplace.interceptor.AuthInterceptor;
import com.campus.marketplace.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse<ReviewResponse> submitReview(@Valid @RequestBody ReviewRequest request,
                                                     HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(reviewService.submitReview(userId, request));
    }

    @GetMapping("/transaction/{transactionId}")
    public ApiResponse<ReviewResponse> getTransactionReview(@PathVariable Long transactionId,
                                                             HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(reviewService.getTransactionReview(transactionId, userId));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ReviewResponse>> getUserReviews(@PathVariable Long userId,
                                                             @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.success(reviewService.getUserReviews(userId, limit));
    }
}
