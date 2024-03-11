package com.share.hy.service;

import com.share.hy.dto.goods.GoodsDTO;

import java.util.List;
import java.util.Map;

public interface IGoodsService {

    Map<String, List<GoodsDTO>> queryByUserId(String userId);
}
