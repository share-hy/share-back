package com.share.hy.dto.pay;

import lombok.Data;

@Data
public class PaymentPreCreateInfoDTO {
    private String tradePlat;
    private String userId;
    private String goodsItemId;
    private Byte orderType;
    private String payload;
    private Byte subscription;
    private Long createTime;
    private Integer launchTimes;

}