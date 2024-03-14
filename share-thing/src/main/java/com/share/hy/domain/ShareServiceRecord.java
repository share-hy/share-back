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
@Table(name = "share_service_record")
public class ShareServiceRecord implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    /**
     * 服务id
     */
    @Column(name = "goods_item_id")
    private String goodsItemId;

    /**
     * 服务类型 0-订购 1-续期 2-更换
     */
    @Column(name = "service_type")
    private Byte serviceType;

    /**
     * 服务状态 0-到期 1-正常使用 2-主动停止，升级服务
     */
    @Column(name = "status")
    private Byte status;

    /**
     * 到期时间
     */
    @Column(name = "expired_time")
    private Date expiredTime;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}