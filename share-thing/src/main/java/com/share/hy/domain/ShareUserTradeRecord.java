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
@Table(name = "share_user_trade_record")
public class ShareUserTradeRecord implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "trade_id")
    private String tradeId;

    @Column(name = "trade_plat")
    private String tradePlat;

    /**
     * 币种
     */
    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * 0收入，1支出
     */
    @Column(name = "type")
    private Byte type;

    /**
     * 0-直接支付或收入 1-增加钱包余额或扣除
     */
    @Column(name = "from")
    private Byte from;

    /**
     * 原因 0-消费 1-邀请收入 2-提现 3-充值
     */
    @Column(name = "cause")
    private Byte cause;

    /**
     * 发生时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}