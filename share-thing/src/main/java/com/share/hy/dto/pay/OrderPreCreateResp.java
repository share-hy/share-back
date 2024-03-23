package com.share.hy.dto.pay;

import lombok.Data;

@Data
public class OrderPreCreateResp {
    private String queryId;
    private String orderId;
    private String payload;
    private String target;
}