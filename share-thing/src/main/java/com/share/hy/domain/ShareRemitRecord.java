package com.share.hy.domain;

import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@Table(name = "share_remit_record")
public class ShareRemitRecord implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    /**
     * 金额
     */
    @Column(name = "amount")
    private Long amount;

    /**
     * 0-汇入 1-汇出
     */
    @Column(name = "operate")
    private Byte operate;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("userId", userId)
                .add("amount", amount)
                .add("operate", operate)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}