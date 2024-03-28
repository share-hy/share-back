package com.share.hy.dto.console;

import com.share.hy.common.enums.TradeCauseEnum;
import com.share.hy.domain.ShareUserTradeRecord;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceChangeDTO {

    private Long changeTime;

    private BigDecimal amount;

    private String cause;

    public BalanceChangeDTO(ShareUserTradeRecord shareUserTradeRecord) {
        this.changeTime = shareUserTradeRecord.getCreateTime().getTime();
        this.amount = shareUserTradeRecord.getAmount();
        this.cause = TradeCauseEnum.getDescByCode(shareUserTradeRecord.getCause());
    }
}
