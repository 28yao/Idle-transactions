package com.campus.marketplace.controller;

import com.campus.marketplace.dto.request.ProductCreateRequest;
import com.campus.marketplace.dto.request.ProductQuery;
import com.campus.marketplace.dto.response.ApiResponse;
import com.campus.marketplace.dto.response.PageResponse;
import com.campus.marketplace.dto.response.ProductResponse;
import com.campus.marketplace.interceptor.AuthInterceptor;
import com.campus.marketplace.service.FileService;
import com.campus.marketplace.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;

    @PostMapping("/images")
    public ApiResponse<Map<String, Object>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ApiResponse.error("请选择图片");
        }
        if (files.length > 9) {
            return ApiResponse.error("最多上传9张图片");
        }
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(fileService.uploadImage(file, "products"));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("urls", urls);
        return ApiResponse.success(data);
    }

    @PostMapping
    public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request,
                                                HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        ProductResponse data = productService.createProduct(userId, request);
        return ApiResponse.success(data);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(@PathVariable("id") Long id,
                                                @Valid @RequestBody ProductCreateRequest request,
                                                HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        ProductResponse data = productService.updateProduct(id, userId, request);
        return ApiResponse.success(data);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> detail(@PathVariable("id") Long id,
                                                HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(productService.getProductDetail(id, userId));
    }

    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> list(ProductQuery query) {
        return ApiResponse.success(productService.listProducts(query));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable("id") Long id,
                                          @RequestBody Map<String, Integer> body,
                                          HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        productService.updateProductStatus(id, userId, body.get("status"));
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id,
                                    HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        productService.deleteProduct(id, userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/user")
    public ApiResponse<PageResponse<ProductResponse>> userList(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute(AuthInterceptor.USER_ID_HEADER);
        return ApiResponse.success(productService.listUserProducts(userId, status, page, size));
    }

    @GetMapping("/recommended")
    public ApiResponse<List<ProductResponse>> recommended(@RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        return ApiResponse.success(productService.getRecommended(limit));
    }

    @GetMapping("/latest")
    public ApiResponse<List<ProductResponse>> latest(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return ApiResponse.success(productService.getLatest(limit));
    }
}
