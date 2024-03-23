package com.share.hy.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//    @AllArgsConstructor
public class AlipayPayload {
    private Order order;
    private SignOrder signOrder;
    private Sign sign;

    @Data
    @AllArgsConstructor
    public static class Order {
        private String out_trade_no;
    }

    @Data
    @AllArgsConstructor
    public static class Sign {
        private String external_agreement_no;
        private String execute_time;
        private String goodsItemId;
        private String totalAmount;
    }

    @Data
    @AllArgsConstructor
    public static class SignOrder {
        private String out_trade_no;
        private String agreement_no;
    }
}
