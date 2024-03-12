package com.share.hy.manager;

import com.share.hy.dto.goods.GoodsDTO;

import java.util.List;

public interface GoodsManager {

    List<GoodsDTO> queryGoods(String userId);
}
