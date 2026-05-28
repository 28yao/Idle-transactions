package com.campus.marketplace.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateRequest {

    @NotBlank(message = "请输入商品标题")
    @Size(max = 50, message = "标题不能超过50个字")
    private String title;

    @NotBlank(message = "请输入商品描述")
    @Size(max = 1000, message = "描述不能超过1000个字")
    private String description;

    @NotBlank(message = "请选择商品分类")
    private String category;

    @NotNull(message = "请输入价格")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "原价不能为负")
    private BigDecimal originalPrice;

    @NotBlank(message = "请选择成色")
    private String condition;

    @NotBlank(message = "请填写交易地点")
    private String location;

    private String campus;

    /**
     * 商品状态：1=立即发布(在售)  0=存为草稿
     */
    @NotNull(message = "请选择发布方式")
    private Integer status;

    /**
     * 图片 URL 列表，最多 9 张
     */
    @Size(max = 9, message = "最多上传9张图片")
    private List<String> images;
}
