package com.share.hy.dto.goods;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsDTO {

    private String goodsItemId;

    private String name;

    private String desc;

    private BigDecimal rawPrice;

    private Integer day;

    private Byte level;

    private Byte goodsStatus;

    private Byte duration;
}
