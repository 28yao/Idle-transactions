package com.campus.marketplace.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductQuery {

    private String keyword;
    private String category;
    private String condition;
    private String campus;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    /** latest / price_asc / price_desc / popular */
    private String sort;
    private Integer page;
    private Integer size;
}
