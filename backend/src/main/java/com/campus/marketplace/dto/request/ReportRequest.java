package com.campus.marketplace.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReportRequest {

    /** 1商品 2用户 */
    @NotNull(message = "请指定举报目标类型")
    private Integer targetType;

    @NotNull(message = "请指定举报目标")
    private Long targetId;

    /** 1虚假信息 2违禁物品 3欺诈 4其他 */
    @NotNull(message = "请选择举报类型")
    private Integer type;

    @NotNull(message = "请填写举报原因")
    @Size(min = 5, max = 500, message = "举报原因长度为5-500字")
    private String reason;
}
