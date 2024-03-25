package com.share.hy.service.impl;

import cn.hutool.core.date.DateTime;
import com.share.hy.common.CustomBusinessException;
import com.share.hy.common.enums.DurationEnum;
import com.share.hy.common.enums.ErrorCodeEnum;
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
        ShareGoodsItem shareGoodsItem = goodsManager.queryByGoodsItemId(goodsItemId);
        if (null == shareGoodsItem){
            throw new CustomBusinessException(ErrorCodeEnum.ERROR_PARAM_WRONG);
        }
        List<ShareServiceRecord> serviceRecordList = goodsManager.queryServiceRecordByUserIdAndStatus(userId, ServiceStatusEnum.NORMAL.getStatus());
        //之前没有购买过，第一次订购
        PurchaseInfoDTO purchaseInfoDTO;
        if (CollectionUtils.isEmpty(serviceRecordList)){
            purchaseInfoDTO = directPurchase(userId,shareGoodsItem);
        }else{
            purchaseInfoDTO = upgradeOrRenewal(userId,serviceRecordList.get(0),shareGoodsItem);
        }
        GoodsDetailDTO goodsDetailDTO = new GoodsDetailDTO();
        String goodsName = goodsManager.getGoodsName(goodsItemId);
        goodsDetailDTO.setServiceName(generateServiceName(goodsName,purchaseInfoDTO.getGoodsStatus()));
        goodsDetailDTO.setGoodsItemId(goodsItemId);
        goodsDetailDTO.setDurationDay(DurationEnum.getDayByDuration(shareGoodsItem.getDuration()));
        return goodsDetailDTO;
    }

    private String generateServiceName(String goodsName, Byte goodsStatus) {
        String desc = GoodsStatusEnum.getDescByCode(goodsStatus);
        return goodsName + "(" + desc + ")";
    }

    /**
     * 升级或续期
     * @param userId
     */
    private PurchaseInfoDTO upgradeOrRenewal(String userId,ShareServiceRecord serviceRecord, ShareGoodsItem thisGoods) {
        PurchaseInfoDTO purchaseInfoDTO = new PurchaseInfoDTO();
        ShareUserAccount userAccount = accountManager.queryAccountByUserId(userId);
        purchaseInfoDTO.setAvailableBalance(null != userAccount ? userAccount.getBalance() : new BigDecimal(0));
        //续期
        if (serviceRecord.getGoodsItemId().equals(thisGoods.getGoodsItemId())){
            purchaseInfoDTO.setGoodsStatus(GoodsStatusEnum.RENEWAL.getCode());
            Date expiredTime = serviceRecord.getExpiredTime();
            Long distanceTime = TimeUtil.getDistanceTime(expiredTime, DurationEnum.getDayByDuration(thisGoods.getDuration()));
            purchaseInfoDTO.setRenewalTime(distanceTime);
            purchaseInfoDTO.setPaymentAmount(thisGoods.getRawPrice());
        }
        //升级的续期时间要跟当前套餐一致
        else{
            ShareGoodsItem existGoods = goodsManager.queryByGoodsItemId(serviceRecord.getGoodsItemId());
            //计算当前服务已使用了多少天，花了多少钱，再用需要升级服务的总价减去，就是需要补的价钱。
            int days = TimeUtil.calculateGap(new DateTime(), serviceRecord.getExpiredTime());
            //当天到期，则全款购买新的套餐，从明天开始计算
            if (days == 0){
                purchaseInfoDTO.setRenewalTime(TimeUtil.getDistanceTime(serviceRecord.getExpiredTime(), DurationEnum.getDayByDuration(thisGoods.getDuration())));
                purchaseInfoDTO.setPaymentAmount(thisGoods.getRawPrice());
            }
            else{
                purchaseInfoDTO.setRenewalTime(TimeUtil.getDistanceTime(serviceRecord.getExpiredTime(), days + DurationEnum.getDayByDuration(thisGoods.getDuration())));
                BigDecimal unitPrice = existGoods.getUnitPrice();
                if (null == unitPrice){
                    throw new CustomBusinessException(ErrorCodeEnum.ERROR_NOT_FIND_UNIT_PRICE);
                }
                BigDecimal usedMoney = unitPrice.multiply(new BigDecimal(days));
                BigDecimal needPay = thisGoods.getRawPrice().subtract(usedMoney);
                purchaseInfoDTO.setPaymentAmount(needPay);
            }
        }
        return purchaseInfoDTO;
    }

    private PurchaseInfoDTO directPurchase(String userId, ShareGoodsItem shareGoodsItem ) {
        PurchaseInfoDTO purchaseInfoDTO = new PurchaseInfoDTO();
        purchaseInfoDTO.setPaymentAmount(shareGoodsItem.getRawPrice());
        long assignTime = TimeUtil.getAssignTime(DurationEnum.getDayByDuration(shareGoodsItem.getDuration()));
        purchaseInfoDTO.setRenewalTime(assignTime);
        purchaseInfoDTO.setGoodsStatus(GoodsStatusEnum.NORMAL.getCode());
        ShareUserAccount userAccount = accountManager.queryAccountByUserId(userId);
        purchaseInfoDTO.setAvailableBalance(null != userAccount ? userAccount.getBalance() : new BigDecimal(0));
        return purchaseInfoDTO;
    }
}
