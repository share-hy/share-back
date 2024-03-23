package com.share.hy.dto.pay;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateParamDTO {
   /**
    * 原始的tradeId（无前缀。）
    */
   private String tradeId;
   private String orderId;
   private String userId;
   private String goodsItemId;
   private Long createTime;
   private Byte type;
   //订单金额
   private BigDecimal totalAmount;
   private Long paymentTime;
   private String currency;
   private String tradePlat;

}
