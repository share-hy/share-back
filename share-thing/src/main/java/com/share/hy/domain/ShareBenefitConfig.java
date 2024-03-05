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
@Table(name = "share_benefit_config")
public class ShareBenefitConfig implements Serializable {
    /**
     * 主键id
     */
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "goods_item_id")
    private String goodsItemId;

    /**
     * 关联级别（上级）
     */
    @Column(name = "level")
    private Byte level;

    /**
     * 分润金额
     */
    @Column(name = "amount")
    private Long amount;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("goodsItemId", goodsItemId)
                .add("level", level)
                .add("amount", amount)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}