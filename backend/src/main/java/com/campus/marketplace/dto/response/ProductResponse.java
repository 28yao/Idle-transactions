package com.campus.marketplace.dto.response;

import com.campus.marketplace.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse {

    private Long id;
    private Long sellerId;
    private String sellerNickname;
    private String sellerAvatar;
    private Integer sellerVerifyStatus;

    private String title;
    private String description;
    private String category;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String condition;
    private String location;
    private String campus;
    private Integer status;
    private Integer viewCount;
    private Integer favCount;
    private String rejectReason;

    private List<String> images;
    private String coverImage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponse fromEntity(Product p) {
        ProductResponse r = new ProductResponse();
        r.setId(p.getId());
        r.setSellerId(p.getSellerId());
        r.setTitle(p.getTitle());
        r.setDescription(p.getDescription());
        r.setCategory(p.getCategory());
        r.setPrice(p.getPrice());
        r.setOriginalPrice(p.getOriginalPrice());
        r.setCondition(p.getCondition());
        r.setLocation(p.getLocation());
        r.setCampus(p.getCampus());
        r.setStatus(p.getStatus());
        r.setViewCount(p.getViewCount());
        r.setFavCount(p.getFavCount());
        r.setRejectReason(p.getRejectReason());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}
