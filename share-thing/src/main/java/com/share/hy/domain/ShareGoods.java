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
@Table(name = "share_goods")
public class ShareGoods implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * id
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 名称（助记，不会展示在app中）
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 服务类型，见iot_order.type
     */
    @Column(name = "type")
    private Byte type;

    /**
     * 商品标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 商品描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 滚动时长
     */
    @Column(name = "rolling_length")
    private Integer rollingLength;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 是否上线该服务
     */
    @Column(name = "enabled")
    private Boolean enabled;

    /**
     * 版本
     */
    @Column(name = "version")
    private Long version;

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("goodsId", goodsId)
                .add("goodsName", goodsName)
                .add("type", type)
                .add("title", title)
                .add("description", description)
                .add("rollingLength", rollingLength)
                .add("sort", sort)
                .add("enabled", enabled)
                .add("version", version)
                .add("remark", remark)
                .add("updateTime", updateTime)
                .add("createTime", createTime)
                .add("updateBy", updateBy)
                .add("createBy", createBy)
                .toString();
    }
}