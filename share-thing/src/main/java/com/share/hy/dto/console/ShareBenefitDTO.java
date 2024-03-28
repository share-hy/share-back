package com.share.hy.dto.console;

import com.share.hy.domain.ShareBenefitRecord;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShareBenefitDTO {

    private String from;
    private Byte level;
    private BigDecimal amount;

    public ShareBenefitDTO(ShareBenefitRecord shareBenefitRecord) {
        this.from = shareBenefitRecord.getFromUser();
        this.level = shareBenefitRecord.getLevel();
        this.amount = shareBenefitRecord.getAmount();
    }
}
