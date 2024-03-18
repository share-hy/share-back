package com.share.hy.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BenefitInfoDTO {

    private Byte subLevel;

    /**
     * 分润
     */
    private BigDecimal shareBenefit;
}
