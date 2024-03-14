package com.share.hy.manager.impl;

import com.share.hy.domain.ShareGoods;
import com.share.hy.domain.ShareGoodsItem;
import com.share.hy.dto.goods.GoodsDTO;
import com.share.hy.manager.GoodsManager;
import com.share.hy.mapper.ShareGoodsItemMapper;
import com.share.hy.mapper.ShareGoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GoodsManagerImpl implements GoodsManager {

    @Autowired
    private ShareGoodsMapper shareGoodsMapper;
    @Autowired
    private ShareGoodsItemMapper shareGoodsItemMapper;

    private static Map<Byte,List<GoodsDTO>> ONLINE_GOODS = null;

    @PostConstruct
    @Override
    public void init() {
        List<ShareGoodsItem> shareGoodsItems = shareGoodsItemMapper.selectAll();
        List<ShareGoods> shareGoods = shareGoodsMapper.selectAll();
        if (CollectionUtils.isEmpty(shareGoodsItems) || CollectionUtils.isEmpty(shareGoods)){
            log.info("no valid data");
            return;
        }
        Map<String, ShareGoods> goodsMap = shareGoods.stream().collect(Collectors.toMap(ShareGoods::getGoodsId, k -> k));
        shareGoodsItems.forEach();
    }

    @Override
    public List<GoodsDTO> queryGoods() {

    }
}
