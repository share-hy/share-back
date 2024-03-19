package com.share.hy.dto.admin;

import com.share.hy.domain.ShareBenefitConfig;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class BenefitInfoDTO {

    @NotBlank
    private String goodsItemId;

    @NotBlank
    private Byte subLevel;

    /**
     * 分润
     */
    @NotBlank
    private BigDecimal shareBenefit;

    public BenefitInfoDTO(ShareBenefitConfig shareBenefitConfig) {
        this.goodsItemId = shareBenefitConfig.getGoodsItemId();
        this.subLevel = shareBenefitConfig.getLevel();
        this.shareBenefit = shareBenefitConfig.getAmount();
    }
}
