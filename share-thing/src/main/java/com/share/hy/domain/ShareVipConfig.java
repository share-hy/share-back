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
@Table(name = "share_vip_config")
public class ShareVipConfig implements Serializable {
    /**
     * 主键id
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 级别
     */
    @Column(name = "level")
    private Byte level;

    /**
     * 发展下级的数量
     */
    @Column(name = "quantity")
    private Integer quantity;

    /**
     * 权益，暂不确定
     */
    @Column(name = "benefit")
    private String benefit;

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

    private static final long serialVersionUID = 1L;

}