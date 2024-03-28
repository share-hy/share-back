package com.share.hy.dto.pay;

import com.share.hy.common.enums.OrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaunchPayDTO {

    private String outTradeNo;

    private PayCreateDTO payCreateDTO;

    private String orderId;

    private BigDecimal price;

    private OrderTypeEnum typeEnum;

    private String expireTime;
}
