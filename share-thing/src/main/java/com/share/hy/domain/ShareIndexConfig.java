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
@Table(name = "share_index_config")
public class ShareIndexConfig implements Serializable {
    /**
     * 主键id
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 0-图片,1-视频 2-客户端地址
     */
    @Column(name = "type")
    private Byte type;

    /**
     * 描述
     */
    @Column(name = "desc")
    private String desc;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Byte sort;

    /**
     * 0-下线 1-上线
     */
    @Column(name = "enable")
    private Byte enable;

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
     * 链接
     */
    @Column(name = "link")
    private String link;

    private static final long serialVersionUID = 1L;
}