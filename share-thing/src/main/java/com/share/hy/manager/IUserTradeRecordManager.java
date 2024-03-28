package com.share.hy.manager;


import com.share.hy.domain.ShareUserTradeRecord;

import java.util.List;

public interface IUserTradeRecordManager {

    int insert(ShareUserTradeRecord orderTradeRecord);

    List<ShareUserTradeRecord> getByOrderIds(List<String> orderIds);

    List<ShareUserTradeRecord> queryBalanceByUserId(String userId);
}
