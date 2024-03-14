package com.share.hy.service.impl;

import com.share.hy.dto.goods.GoodsDTO;
import com.share.hy.manager.GoodsManager;
import com.share.hy.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IGoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsManager goodsManager;

    @Override
    public Map<String, List<GoodsDTO>> queryByUserId(String userId) {
        List<GoodsDTO> goodsDTOS = goodsManager.queryGoods();
        return null;
    }
}
