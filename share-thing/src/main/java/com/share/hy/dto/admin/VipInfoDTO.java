package com.share.hy.dto.admin;

import com.share.hy.domain.ShareVipConfig;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VipInfoDTO {

    /**
     * 等级
     */
    @NotBlank
    private Byte level;

    /**
     * 下级数量
     */
    @NotBlank
    private Integer quantity;

    /**
     * 优惠政策
     */
    @NotBlank
    private String benefit;

    public VipInfoDTO(ShareVipConfig vipConfig) {
        this.level = vipConfig.getLevel();
        this.quantity = vipConfig.getQuantity();
        this.benefit = vipConfig.getBenefit();
    }
}
