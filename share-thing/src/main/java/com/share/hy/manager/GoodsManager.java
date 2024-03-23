package com.share.hy.manager;

import com.share.hy.domain.ShareGoods;
import com.share.hy.domain.ShareGoodsItem;
import com.share.hy.domain.ShareServiceRecord;
import com.share.hy.dto.goods.GoodsDTO;

import java.util.List;

public interface GoodsManager {

    void init();

    List<GoodsDTO> queryGoods();

    List<ShareServiceRecord> queryServiceRecordByUserIdAndStatus(String userId,Byte status);

    ShareGoodsItem queryByGoodsItemId(String goodsItemId);

    Byte getLevelByGoodsItemId(String goodsItemId);

    String getGoodsName(String goodsItemId);

    List<ShareGoodsItem> queryByGoodsItemIds(List<String> goodsItemIds);
}
