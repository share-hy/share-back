package com.share.hy.dto.goods;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsDetailDTO {

    private String goodsItemId;

    private String serviceName;

    private Long renewalTime;

    private BigDecimal availableBalance;

    private Integer durationDay;

    private BigDecimal rawPrice;

    private BigDecimal paymentAmount;
}
