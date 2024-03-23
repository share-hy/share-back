package com.share.hy.dto.pay;

import com.share.hy.common.enums.OrderTypeEnum;
import lombok.Data;

@Data
public class AlipayOrderCreateDTO {
    private String orderId;
    private String totalAmount;
    private String title;
    private OrderTypeEnum orderType;

    /**
     * 二维码大小，支付宝选用二维码支付时需要该字段
     */
    private Integer qrcodeWidth;
    /**
     * yyyy-MM-dd HH:mm:ss  +8时区；
     */
    private String timeExpire;

    private AlipayAttachmentDTO passbackParam;
}
