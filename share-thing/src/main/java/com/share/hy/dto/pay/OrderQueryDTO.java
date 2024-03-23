package com.share.hy.dto.pay;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class OrderQueryDTO{

    private String userId;


    /**
     * {@link com.share.hy.common.enums.OrderStatusEnum}
     */
    private List<Byte> status;
}
