package com.share.hy.dto.pay;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PayCreateDTO {

    private String userId;

    @NotBlank(message = "trade can't blank.")
    private String tradePlat;

    /**
     * 是否使用余额
     */
    private Byte useBalance;

    /**
     * PayModeEnum
     */
    private String payMode;

    /**
     * 使用支付宝二维码支付，需要传入该字段
     */
    private Integer qrcodeWidth;

    private Byte type;

    /**
     * 商品id，仅用于推送到金蝶使用
     */
    private String goodsItemId;
}