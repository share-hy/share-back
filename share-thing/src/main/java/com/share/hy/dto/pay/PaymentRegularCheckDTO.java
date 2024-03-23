package com.share.hy.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRegularCheckDTO {
    private String tradePlat;
    private String queryId;
    private Integer retryTimes;
}
