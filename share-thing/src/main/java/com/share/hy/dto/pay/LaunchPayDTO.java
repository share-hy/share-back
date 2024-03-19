package com.share.hy.dto.pay;

import com.share.hy.common.enums.OrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Dyate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaunchPayDTO {

    private String outTradeNo;

    private BigDecimal rawPrice;

    private String goodsItemId;

    private String orderId;

    private String userId;

    private OrderTypeEnum typeEnum;

    private String payMode;

    private Integer qrcodeWidth;

    private String expireTime;
}
