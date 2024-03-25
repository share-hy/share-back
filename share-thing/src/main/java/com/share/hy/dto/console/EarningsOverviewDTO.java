package com.share.hy.dto.console;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EarningsOverviewDTO {

    private BigDecimal totalAmount;
    private List<IncomeGroupInfo> group;

    @Data
    private static class IncomeGroupInfo{
        private BigDecimal amount;
        private Integer level;
    }
}
