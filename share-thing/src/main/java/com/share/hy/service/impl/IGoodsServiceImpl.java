package com.share.hy.service.impl;

import com.share.hy.common.enums.DurationEnum;
import com.share.hy.common.enums.GoodsStatusEnum;
import com.share.hy.common.enums.ServiceStatusEnum;
import com.share.hy.domain.ShareGoodsItem;
import com.share.hy.domain.ShareServiceRecord;
import com.share.hy.domain.ShareUserAccount;
import com.share.hy.dto.goods.GoodsDTO;
import com.share.hy.dto.goods.GoodsDetailDTO;
import com.share.hy.dto.goods.PurchaseInfoDTO;
import com.share.hy.manager.GoodsManager;
import com.share.hy.manager.IAccountManager;
import com.share.hy.service.IGoodsService;
import com.share.hy.utils.TimeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IGoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsManager goodsManager;
    @Autowired
    private IAccountManager accountManager;

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
                    GoodsStatusEnum goodsStatusEnum = judgeGoodsStatus(existLevel, level);
                    goods.setGoodsStatus(goodsStatusEnum.getCode());
                }
            });
        });
        return resultMap;
    }

    private GoodsStatusEnum judgeGoodsStatus(Byte existLevel, Byte level) {
        if (existLevel > level){
            return GoodsStatusEnum.NO_SUPPORT;
        }
        else if(existLevel.equals(level)){
            return GoodsStatusEnum.RENEWAL;
        }
        else {
            return GoodsStatusEnum.UPGRADE;
        }
    }

    @Override
    public GoodsDetailDTO detail(String userId, String goodsItemId) {
        List<ShareServiceRecord> serviceRecordList = goodsManager.queryServiceRecordByUserIdAndStatus(userId, ServiceStatusEnum.NORMAL.getStatus());
        //之前没有购买过，第一次订购
        PurchaseInfoDTO purchaseInfoDTO;
        if (CollectionUtils.isEmpty(serviceRecordList)){
            purchaseInfoDTO = directPurchase(userId,goodsItemId);
        }else{
            purchaseInfoDTO = upgradeOrRenewal(userId,serviceRecordList.get(0),goodsItemId);
        }

    }

    /**
     * 升级或续期
     * @param userId
     * @param goodsItemId
     */
    private PurchaseInfoDTO upgradeOrRenewal(String userId,ShareServiceRecord serviceRecord, String goodsItemId) {
        ShareUserAccount userAccount = accountManager.queryAccountByUserId(userId);
        PurchaseInfoDTO purchaseInfoDTO = new PurchaseInfoDTO();
        purchaseInfoDTO.setAvailableBalance(null != userAccount ? userAccount.getBalance() : new BigDecimal(0));
        ShareGoodsItem thisGoods = goodsManager.queryByGoodsItemId(goodsItemId);
        //续期
        if (serviceRecord.getGoodsItemId().equals(goodsItemId)){
            Date expiredTime = serviceRecord.getExpiredTime();
            Long distanceTime = TimeUtil.getDistanceTime(expiredTime, DurationEnum.getDayByDuration(thisGoods.getDuration()));
            purchaseInfoDTO.setRenewalTime(distanceTime);
            purchaseInfoDTO.setPaymentAmount(thisGoods.getRawPrice());
        }
        //升级
        else{
            ShareGoodsItem existGoods = goodsManager.queryByGoodsItemId(serviceRecord.getGoodsItemId());

        }
    }

    private PurchaseInfoDTO directPurchase(String userId, String goodsItemId) {
        ShareGoodsItem shareGoodsItem = goodsManager.queryByGoodsItemId(goodsItemId);
        PurchaseInfoDTO purchaseInfoDTO = new PurchaseInfoDTO();
        purchaseInfoDTO.setPaymentAmount(shareGoodsItem.getRawPrice());
        long assignTime = TimeUtil.getAssignTime(DurationEnum.getDayByDuration(shareGoodsItem.getDuration()));
        purchaseInfoDTO.setRenewalTime(assignTime);
        ShareUserAccount userAccount = accountManager.queryAccountByUserId(userId);
        purchaseInfoDTO.setAvailableBalance(null != userAccount ? userAccount.getBalance() : new BigDecimal(0));
        return purchaseInfoDTO;
    }
}
