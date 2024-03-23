package com.share.hy.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@Table(name = "share_order")
public class ShareOrder implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 订单状态
     * -4 订单已超过可支付的时间
     * -3 订单被取消
     * -2-异常
     * -1-关闭
     * 0-创建
     * 1-支付中
     * 2-支付完成
     * 3-支付失败
     * 4-退款中
     * 5-已退款
     * 如果状态是关闭/异常，cause字段可能会有数据来备注原因。
     */
    @Column(name = "status")
    private Byte status;

    /**
     * 付款金额
     */
    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private Date payTime;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 商品id
     */
    @Column(name = "goods_item_id")
    private String goodsItemId;

    /**
     * 第三方交易单id,
     */
    @Column(name = "trade_id")
    private String tradeId;

    /**
     * 交易平台
     */
    @Column(name = "trade_plat")
    private String tradePlat;

    /**
     * 收款币种
     */
    @Column(name = "currency")
    private String currency;

    /**
     * 下单时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 异常/关闭原因。
     */
    @Column(name = "cause")
    private String cause;

    @Column(name = "remark")
    private String remark;

    /**
     * 退款时间，当有退款发生时不为空。
     */
    @Column(name = "refund_time")
    private Date refundTime;

    /**
     * 退款币种
     */
    @Column(name = "refund_currency")
    private String refundCurrency;

    /**
     * 退款金额，当有退款发生时不为空。
     */
    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    /**
     * 最后一次更新时间
     */
    @Column(name = "latest_update_time")
    private Date latestUpdateTime;

    private static final long serialVersionUID = 1L;
}