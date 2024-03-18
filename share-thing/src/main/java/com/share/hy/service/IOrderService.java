package com.share.hy.service;

import com.share.hy.dto.pay.OrderPreCreateResp;
import com.share.hy.dto.pay.PayCreateDTO;

public interface IOrderService {

    OrderPreCreateResp preCreateOrder(PayCreateDTO payCreateDTO);
}
