package com.campus.marketplace.controller;

import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.dto.response.PageResponse;
import com.campus.marketplace.entity.Product;
import com.campus.marketplace.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> dashboard() {
        return ApiResponse.success(adminService.getDashboard());
    }

    @GetMapping("/users")
    public ApiResponse<PageResponse<Map<String, Object>>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(adminService.getUsers(page, size));
    }

    @PutMapping("/users/{id}/status")
    public ApiResponse<Void> updateUserStatus(@PathVariable("id") Long id,
                                              @RequestBody Map<String, Integer> body) {
        adminService.updateUserStatus(id, body.get("status"));
        return ApiResponse.success(null);
    }

    @GetMapping("/products")
    public ApiResponse<PageResponse<Product>> getProducts(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(adminService.getProducts(status, page, size));
    }

    @PutMapping("/products/{id}/status")
    public ApiResponse<Void> updateProductStatus(@PathVariable("id") Long id,
                                                 @RequestBody Map<String, Integer> body) {
        adminService.updateProductStatus(id, body.get("status"));
        return ApiResponse.success(null);
    }

    @GetMapping("/verifications")
    public ApiResponse<PageResponse<Map<String, Object>>> getPendingVerifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(adminService.getPendingVerifications(page, size));
    }

    @PutMapping("/verifications/{id}")
    public ApiResponse<Void> reviewVerification(@PathVariable("id") Long id,
                                                @RequestBody Map<String, Boolean> body) {
        adminService.reviewVerification(id, body.get("approved"));
        return ApiResponse.success(null);
    }

    @GetMapping("/reports")
    public ApiResponse<PageResponse<Map<String, Object>>> getReports(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(adminService.getReports(status, page, size));
    }

    @PutMapping("/reports/{id}")
    public ApiResponse<Void> reviewReport(@PathVariable("id") Long id,
                                          @RequestBody Map<String, Boolean> body) {
        adminService.reviewReport(id, body.get("approved"));
        return ApiResponse.success(null);
    }
}
