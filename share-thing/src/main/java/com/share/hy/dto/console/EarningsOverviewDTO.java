package com.share.hy.dto.console;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class EarningsOverviewDTO {

    private BigDecimal totalAmount;
    private List<IncomeGroupInfo> group;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IncomeGroupInfo{
        private BigDecimal amount;
        private Byte level;
    }

    public EarningsOverviewDTO(BigDecimal totalAmount, List<IncomeGroupInfo> group) {
        this.totalAmount = totalAmount;
        this.group = group;
    }
}
