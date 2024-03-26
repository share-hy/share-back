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
@Table(name = "share_user_relation")
public class ShareUserRelation implements Serializable {
    /**
     * 主键id
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
     * 第一上级
     */
    @Column(name = "first")
    private String first;

    /**
     * 第二上级
     */
    @Column(name = "second")
    private String second;

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

    public ShareUserRelation(String userId, String first) {
        this.userId = userId;
        this.first = first;
    }

    private static final long serialVersionUID = 1L;
}