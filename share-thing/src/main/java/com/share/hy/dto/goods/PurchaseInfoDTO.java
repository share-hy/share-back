package com.share.hy.dto.goods;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseInfoDTO {

    private Long renewalTime;

    private BigDecimal availableBalance;

    /**
     * 新增时长天数
     */
    private Integer addDuration;

    private BigDecimal paymentAmount;

    private Byte goodsStatus;
}
