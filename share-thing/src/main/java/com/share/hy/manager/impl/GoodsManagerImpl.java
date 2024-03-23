package com.share.hy.manager.impl;

import com.share.hy.common.enums.DurationEnum;
import com.share.hy.common.enums.GoodsStatusEnum;
import com.share.hy.domain.ShareGoods;
import com.share.hy.domain.ShareGoodsItem;
import com.share.hy.domain.ShareServiceRecord;
import com.share.hy.dto.goods.GoodsDTO;
import com.share.hy.manager.GoodsManager;
import com.share.hy.mapper.ShareGoodsItemMapper;
import com.share.hy.mapper.ShareGoodsMapper;
import com.share.hy.mapper.ShareServiceRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GoodsManagerImpl implements GoodsManager {

    @Autowired
    private ShareGoodsMapper shareGoodsMapper;
    @Autowired
    private ShareGoodsItemMapper shareGoodsItemMapper;
    @Autowired
    private ShareServiceRecordMapper shareServiceRecordMapper;

    private static final Map<Byte,List<GoodsDTO>> ONLINE_GOODS = new HashMap<>();

    private static final Map<String,ShareGoodsItem> GOODS_ITEM_MAP = new HashMap<>();

    private static Map<String,ShareGoods> GOODS_MAP = new HashMap<>();

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
        GOODS_MAP = goodsMap;
        shareGoodsItems.forEach(goodItem ->{
            ShareGoods goods = goodsMap.get(goodItem.getGoodsId());
            GoodsDTO goodsDTO = new GoodsDTO();
            goodsDTO.setName(goods.getGoodsName());
            goodsDTO.setDesc(goodsDTO.getDesc());
            goodsDTO.setGoodsStatus(GoodsStatusEnum.NORMAL.getCode());
            goodsDTO.setLevel(goods.getLevel());
            GOODS_ITEM_MAP.put(goodItem.getGoodsId(),goodItem);
            goodsDTO.setGoodsItemId(goodItem.getGoodsItemId());
            goodsDTO.setDuration(goodItem.getDuration());
            goodsDTO.setDay(DurationEnum.getDayByDuration(goodItem.getDuration()));
            ONLINE_GOODS.computeIfAbsent(goods.getType(),k->new LinkedList<>()).add(goodsDTO);
        });
    }

    /**
     * 默认返回VPN商品-0
     * @return
     */
    @Override
    public List<GoodsDTO> queryGoods() {
        return ONLINE_GOODS.get((byte)0);
    }

    @Override
    public List<ShareServiceRecord> queryServiceRecordByUserIdAndStatus(String userId,Byte status) {
        Example example = new Example(ShareServiceRecord.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("userId", userId);
        if (null != status){
            criteria.andEqualTo("status",status);
        }
        return shareServiceRecordMapper.selectByExample(example);
    }

    @Override
    public ShareGoodsItem queryByGoodsItemId(String goodsItemId) {
        return GOODS_ITEM_MAP.get(goodsItemId);
    }

    @Override
    public Byte getLevelByGoodsItemId(String goodsItemId) {
        ShareGoodsItem shareGoodsItem = GOODS_ITEM_MAP.get(goodsItemId);
        if (null == shareGoodsItem){
            return null;
        }
        ShareGoods shareGoods = GOODS_MAP.get(shareGoodsItem.getGoodsId());
        if (null == shareGoods){
            return null;
        }
        return shareGoods.getLevel();
    }

    @Override
    public String getGoodsName(String goodsItemId) {
        ShareGoodsItem goodsItem = GOODS_ITEM_MAP.get(goodsItemId);
        if (null == goodsItem){
            return "";
        }
        ShareGoods shareGoods = GOODS_MAP.get(goodsItem.getGoodsId());
        if (null == shareGoods){
            return "";
        }
        return shareGoods.getGoodsName();
    }

    @Override
    public List<ShareGoodsItem> queryByGoodsItemIds(List<String> goodsItemIds) {
        if (CollectionUtils.isEmpty(goodsItemIds)){
            return Collections.emptyList();
        }
        return goodsItemIds.stream().map(GOODS_ITEM_MAP::get).collect(Collectors.toList());
    }
}
