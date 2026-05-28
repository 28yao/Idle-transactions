package com.campus.marketplace.controller;

import com.campus.marketplace.dto.request.ReportRequest;
import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.interceptor.AuthInterceptor;
import com.campus.marketplace.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody ReportRequest request,
                                     HttpServletRequest httpRequest) {
        Long reporterId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        reportService.createReport(reporterId, request);
        return ApiResponse.success("举报成功，我们将尽快处理", null);
    }
}
