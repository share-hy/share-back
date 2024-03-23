package com.share.hy.service;

import com.share.hy.domain.ShareOrder;
import com.share.hy.dto.pay.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IOrderService {

    OrderPreCreateResp preCreateOrder(PayCreateDTO payCreateDTO);

    int orderCompleted(String orderId, Date payTime);

    List<OrderInfoDTO> queryOrder(OrderQueryDTO queryDTO);

    void orderCompletedDirectly(ShareOrder toInsertOrder, boolean update);

    int closeOrder(String orderId);

    int closeOrder(ShareOrder order);

    void refund(String orderId, BigDecimal refundedAmount, String currency, Date refundTime);

    void refund(ShareOrder order, BigDecimal refundedAmount, String currency, Date refundTime);

    void postCreationOrder(OrderCreateParamDTO postCreateOrderParam);

    void periodicCheck(PaymentRegularCheckDTO paymentRegularCheckDTO);
}
