package com.share.hy.manager;

import com.share.hy.domain.ShareOrder;

import java.util.List;

public interface IOrderManager {

    int insert(ShareOrder order);

    int updateOrderState(String orderId, Byte orderStatus);

    ShareOrder selByOrderId(String orderId);
    List<ShareOrder> selByOrderId(List<String> orderIds);

    ShareOrder selByTradeId(String tradeId);

    int updateByOrderId(ShareOrder order);

    ShareOrder selByUserIdAndOrderId(String userId, String orderId);

    List<ShareOrder> selByUserIdAndStatus(String userId, List<Byte> status);
}
