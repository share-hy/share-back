package com.share.hy.service;

import com.share.hy.dto.console.ServiceChangeDTO;
import com.share.hy.dto.goods.GoodsDTO;
import com.share.hy.dto.goods.GoodsDetailDTO;

import java.util.List;
import java.util.Map;

public interface IGoodsService {

    Map<String, List<GoodsDTO>> queryByUserId(String userId);

    /**
     * 购买信息
     * @param userId
     * @param goodsItemId
     * @return
     */
    GoodsDetailDTO detail(String userId, String goodsItemId);

    List<ServiceChangeDTO> queryService(String userId);
}
