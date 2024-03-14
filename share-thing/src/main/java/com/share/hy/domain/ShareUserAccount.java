package com.share.hy.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@Table(name = "share_user_account")
public class ShareUserAccount implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 余额
     */
    @Column(name = "balance")
    private Long balance;

    /**
     * 上一次充值的时间
     */
    @Column(name = "last_recharge_time")
    private Date lastRechargeTime;

    /**
     * 上一次提款的时间
     */
    @Column(name = "last_withdrawal_time")
    private Date lastWithdrawalTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}