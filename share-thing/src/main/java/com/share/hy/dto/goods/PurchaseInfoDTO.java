package com.share.hy.dto.goods;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseInfoDTO {

    private Long renewalTime;

    private BigDecimal availableBalance;

    private BigDecimal paymentAmount;
}
