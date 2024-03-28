package com.share.hy.dto.pay;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentOrderCompletedParam {

   private OrderInfo orderInfo;

   /**
    * 原始的tradeId（无前缀。）
    */
   private String tradeId;
   /**
    * 退款金额
    */
   private BigDecimal totalAmount;
   private Long paymentTime;

   /**
    * {@link com.share.hy.common.enums.OrderTypeEnum}
    */
   private Byte type;
   private String currency;
   private String tradePlat;

   private Object rawMsg;

   @Data
   public static class OrderInfo{
      private String orderId;
      private String userId;
      private String goodsItemId;
      private Long createTime;
      private String themeId;
   }
}
