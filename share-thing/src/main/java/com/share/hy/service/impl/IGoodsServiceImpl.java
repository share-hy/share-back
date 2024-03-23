package com.share.hy.service.impl;

import com.share.hy.common.enums.GoodsStatusEnum;
import com.share.hy.common.enums.ServiceStatusEnum;
import com.share.hy.domain.ShareServiceRecord;
import com.share.hy.dto.goods.GoodsDTO;
import com.share.hy.dto.goods.GoodsDetailDTO;
import com.share.hy.manager.GoodsManager;
import com.share.hy.service.IGoodsService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IGoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsManager goodsManager;

    @Override
    public Map<String, List<GoodsDTO>> queryByUserId(String userId) {
        List<GoodsDTO> goodsDTOS = goodsManager.queryGoods();
        if (CollectionUtils.isEmpty(goodsDTOS)){
            return Collections.emptyMap();
        }
        Map<Byte, List<GoodsDTO>> groupByDuration = goodsDTOS.stream().collect(Collectors.groupingBy(GoodsDTO::getDuration));
        Map<String, List<GoodsDTO>> resultMap = new HashMap<>();
        groupByDuration.forEach((k,v)->{
            resultMap.put(GoodsStatusEnum.getDescByCode(k),v);
        });
        List<ShareServiceRecord> serviceRecordList = goodsManager.queryServiceRecordByUserIdAndStatus(userId, ServiceStatusEnum.NORMAL.getStatus());
        if (CollectionUtils.isEmpty(serviceRecordList)){
            return resultMap;
        }
        ShareServiceRecord serviceRecord = serviceRecordList.get(0);
        Byte existLevel = goodsManager.getLevelByGoodsItemId(serviceRecord.getGoodsItemId());
        if (null == existLevel){
            return resultMap;
        }
        resultMap.forEach((k,v) ->{
            v.forEach(goods ->{
                String goodsItemId = goods.getGoodsItemId();
                Byte level = goodsManager.getLevelByGoodsItemId(goodsItemId);
                if (null != level){
                    byte goodsStatus;
                    if (existLevel > level){
                        goodsStatus = GoodsStatusEnum.NO_SUPPORT.getCode();
                    }
                    else if(existLevel.equals(level)){
                        goodsStatus = GoodsStatusEnum.RENEWAL.getCode();
                    }
                    else {
                        goodsStatus = GoodsStatusEnum.UPGRADE.getCode();
                    }
                    goods.setGoodsStatus(goodsStatus);
                }
            });
        });
        return resultMap;
    }

    @Override
    public GoodsDetailDTO detail(String userId, String goodsItemId) {
        List<ShareServiceRecord> serviceRecordList = goodsManager.queryServiceRecordByUserIdAndStatus(userId, ServiceStatusEnum.NORMAL.getStatus());
        //之前没有购买过，第一次订购
        if (CollectionUtils.isEmpty(serviceRecordList)){
            directPurchase(userId,goodsItemId);
        }else{
            upgradeOrRenewal(userId,serviceRecordList.get(0),goodsItemId);
        }

    }

    /**
     * 升级或续期
     * @param userId
     * @param goodsItemId
     */
    private void upgradeOrRenewal(String userId,ShareServiceRecord serviceRecord, String goodsItemId) {

    }

    private void directPurchase(String userId, String goodsItemId) {

    }
}
