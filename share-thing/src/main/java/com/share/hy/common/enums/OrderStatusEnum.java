package com.share.hy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    OVERTIME((byte)-4,"订单已超过可支付的时间"),
    CANCEL((byte)-3,"订单被取消，通常是用户主动发起"),
    ABNORMAL((byte)-2,"异常"),
    CLOSED((byte)-1,"已关闭"),
    CREATED((byte)0,"已创建"),
    PAYING((byte)1,"支付中"),
    PAY_SUCCESSFULLY((byte)2,"已发货"),
    PAY_UNSUCCESSFULLY((byte)3,"支付失败"),
    REFUNDING((byte)4,"退款中"),
    REFUNDED((byte)5,"退款成功"),

    ;
    private final byte status;
    private final String description;


}
