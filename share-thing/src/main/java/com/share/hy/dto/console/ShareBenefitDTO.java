package com.share.hy.dto.console;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShareBenefitDTO {

    private String from;
    private Byte level;
    private BigDecimal amount;
}
