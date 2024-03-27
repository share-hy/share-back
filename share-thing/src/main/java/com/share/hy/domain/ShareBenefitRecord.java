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
@Table(name = "share_benefit_record")
public class ShareBenefitRecord implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 产生分润的用户
     */
    @Column(name = "from_user")
    private String fromUser;

    /**
     * 收到分润的用户
     */
    @Column(name = "to_user")
    private String toUser;

    /**
     * 用户级别，仅1级与2级
     */
    @Column(name = "level")
    private Byte level;

    /**
     * 收到的分润金额
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 由哪个订单产生
     */
    @Column(name = "order_id")
    private String orderId;

    private static final long serialVersionUID = 1L;
}