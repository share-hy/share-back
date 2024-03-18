package com.share.hy.dto.admin;

import lombok.Data;

@Data
public class VipInfoDTO {

    /**
     * 等级
     */
    private Byte level;

    /**
     * 下级数量
     */
    private Integer quantity;

    /**
     * 优惠政策
     */
    private String benefit;


}
