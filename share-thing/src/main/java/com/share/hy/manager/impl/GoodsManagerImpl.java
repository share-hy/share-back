package com.share.hy.manager.impl;

import com.share.hy.dto.goods.GoodsDTO;
import com.share.hy.manager.GoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoodsManagerImpl implements GoodsManager {

    @Autowired


    @Override
    public List<GoodsDTO> queryGoods(String userId) {

    }
}
