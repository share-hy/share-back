package com.share.hy.manager.impl;

import com.share.hy.common.enums.DurationEnum;
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
import java.util.HashMap;
import java.util.LinkedList;
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
    @Autowired
    private ShareServiceRecordMapper shareServiceRecordMapper;

    private static Map<Byte,List<GoodsDTO>> ONLINE_GOODS = new HashMap<>();

    private static Map<String,String> GOODS_TO_NAME = new HashMap<>();

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
        shareGoodsItems.forEach(goodItem ->{
            ShareGoods goods = goodsMap.get(goodItem.getGoodsId());
            GoodsDTO goodsDTO = new GoodsDTO();
            goodsDTO.setName(goods.getGoodsName());
            goodsDTO.setDesc(goodsDTO.getDesc());
            GOODS_TO_NAME.put(goodItem.getGoodsId(),goods.getGoodsName());
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
    public List<ShareServiceRecord> queryServiceRecordByUserId(String userId) {
        Example example = new Example(ShareServiceRecord.class);
        example.createCriteria().andEqualTo("userId",userId);
        return shareServiceRecordMapper.selectByExample(example);
    }

    @Override
    public ShareGoodsItem queryByGoodsItemId(String goodsItemId) {
        Example example = new Example(ShareGoodsItem.class);
        example.createCriteria().andEqualTo("goodsItemId",goodsItemId);

        return shareGoodsItemMapper.selectOneByExample(example);

    }

    @Override
    public String getGoodsName(String goodsItemId) {
        return GOODS_TO_NAME.get(goodsItemId);
    }

    @Override
    public List<ShareGoodsItem> queryByGoodsItemIds(List<String> goodsItemIds) {
        Example example = new Example(ShareGoodsItem.class);
        example.createCriteria().andIn("goodsItemId",goodsItemIds);
        return shareGoodsItemMapper.selectByExample(example);
    }
}
