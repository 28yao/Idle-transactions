package com.campus.marketplace.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MessageSendRequest {

    @NotNull(message = "会话ID不能为空")
    private Long conversationId;

    @NotBlank(message = "消息内容不能为空")
    @Size(max = 1000, message = "消息内容不能超过1000字")
    private String content;

    /** 1文字 2图片 3出价 4还价 */
    private Integer type;

    /** 出价金额（type=3/4时） */
    private BigDecimal priceOffer;
}
