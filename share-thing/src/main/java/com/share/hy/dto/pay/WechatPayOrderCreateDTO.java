package com.share.hy.dto.pay;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class WechatPayOrderCreateDTO {
    private BigDecimal total;
    private String currency;
    private String desc;
    private String outTradeNo;
    private String ip;
    //private String type;
    private OffsetDateTime timeExpire;

    private String attach;
}
