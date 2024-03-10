package com.share.hy.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class PaymentCheckDTO {

    /**
     * Unknown(0),
     *
     */
    private Integer status;
    private OrderInfo orderInfo;

    @Data
    public static class OrderInfo{
        private String orderId;
        private Long createTime;
    }


    @Getter
    @AllArgsConstructor
    public enum Status{
        UNKNOWN(0),
        TRADE_SUCCESS(1),
        ;

        private final int code;
    }

}
