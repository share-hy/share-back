package com.share.hy.manager;

import com.share.hy.domain.ShareServiceRecord;
import com.share.hy.dto.goods.GoodsDTO;

import java.util.List;

public interface GoodsManager {

    void init();

    List<GoodsDTO> queryGoods();

    List<ShareServiceRecord> queryServiceRecordByUserId(String userId);

}
