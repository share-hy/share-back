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
@Table(name = "share_goods_item")
public class ShareGoodsItem implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "goods_item_id")
    private String goodsItemId;

    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 原价
     */
    @Column(name = "raw_price")
    private BigDecimal rawPrice;

    /**
     * 币种
     */
    @Column(name = "currency")
    private String currency;

    /**
     * 0-包月(31天) 1-包季度(92天) 2-包年(366天)
     */
    @Column(name = "duration")
    private Byte duration;

    /**
     * 是否启用该服务子项
     */
    @Column(name = "enabled")
    private Boolean enabled;

    /**
     * 排序字段
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    private static final long serialVersionUID = 1L;
}