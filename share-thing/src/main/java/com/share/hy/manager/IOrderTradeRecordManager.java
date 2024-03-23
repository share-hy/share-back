package com.share.hy.manager;


import com.share.hy.domain.ShareUserTradeRecord;

import java.util.List;

public interface IOrderTradeRecordManager {

    int insert(ShareUserTradeRecord orderTradeRecord);

    List<ShareUserTradeRecord> getByOrderIds(List<String> orderIds);
}
