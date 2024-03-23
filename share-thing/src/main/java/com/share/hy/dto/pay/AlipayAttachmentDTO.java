package com.share.hy.dto.pay;

import lombok.Data;

@Data
public class AlipayAttachmentDTO {
    private String orderId;
    private String userId;
    private String goodsItemId;
    private Byte orderType;
}
