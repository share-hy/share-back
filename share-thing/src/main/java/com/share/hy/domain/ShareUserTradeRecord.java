package com.share.hy.domain;

import com.google.common.base.MoreObjects;
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
     * 发生时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("orderId", orderId)
                .add("tradeId", tradeId)
                .add("tradePlat", tradePlat)
                .add("currency", currency)
                .add("amount", amount)
                .add("type", type)
                .add("createTime", createTime)
                .toString();
    }
}