package com.share.hy.dto.goods;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsDetailDTO extends PurchaseInfoDTO {

    private String goodsItemId;

    private String serviceName;

    private Integer durationDay;

    private BigDecimal rawPrice;
}
