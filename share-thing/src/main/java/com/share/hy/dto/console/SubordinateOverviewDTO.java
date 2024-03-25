package com.share.hy.dto.console;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SubordinateOverviewDTO {

    private Integer total;
    private List<PersonalGroupInfo> group;

    @Data
    private static class PersonalGroupInfo{
        private Integer count;
        private Integer level;
    }
}
