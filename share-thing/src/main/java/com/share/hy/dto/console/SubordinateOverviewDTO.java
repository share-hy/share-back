package com.share.hy.dto.console;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubordinateOverviewDTO {

    private Integer total;
    private List<PersonalGroupInfo> group;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonalGroupInfo{
        private Integer count;
        private Byte level;
    }
}
