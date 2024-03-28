package com.share.hy.service.pay;

import com.share.hy.common.CustomBusinessException;
import com.share.hy.common.enums.CurrencyEnum;
import com.share.hy.common.enums.ErrorCodeEnum;
import com.share.hy.common.enums.PaymentPlatEnum;
import com.share.hy.domain.ShareOrder;
import com.share.hy.dto.pay.*;
import com.share.hy.manager.GoodsManager;
import com.share.hy.manager.IOrderManager;
import com.share.hy.service.IOrderService;
import com.share.hy.service.pay.callback.ResendMsgService;
import com.share.hy.service.pay.trade.WechatPayTrader;
import com.share.hy.utils.CacheUtil;
import com.share.hy.utils.SpringRequestHolderUtil;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@Slf4j
public class WechatPayService implements PaymentService {

    @Autowired
    private WechatPayTrader wechatPayTrader;

    @Autowired
    private IOrderManager orderManager;

    @Autowired
    private GoodsManager goodsItemManager;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private ResendMsgService resendMsgService;

    @Autowired
    private IOrderService orderService;

    @Override
    public PaymentPlatEnum getTradePlat() {
        return PaymentPlatEnum.WECHAT_PAY;
    }

    /**
     * 此函数必须在web环境下调用（其中需要获取某些头）
     */
    @Override
    public String launchPay(LaunchPayDTO launchPayDTO) {
        String userIp = SpringRequestHolderUtil.getRequest().getHeader("x-real-ip");

        LocalDateTime parse = LocalDateTime.parse(launchPayDTO.getExpireTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        WechatPayOrderCreateDTO wechatPayOrderCreateDTO = new WechatPayOrderCreateDTO();
        //需要转换为分
        wechatPayOrderCreateDTO.setTotal(launchPayDTO.getPrice().multiply(new BigDecimal("100")));
        wechatPayOrderCreateDTO.setCurrency(CurrencyEnum.CNY.getCode());
        wechatPayOrderCreateDTO.setDesc(launchPayDTO.getOrderId() + "-" + launchPayDTO.getTypeEnum().getDescription());
        wechatPayOrderCreateDTO.setOutTradeNo(launchPayDTO.getOutTradeNo());
        wechatPayOrderCreateDTO.setIp(userIp);
        wechatPayOrderCreateDTO.setTimeExpire(OffsetDateTime.of(parse,ZoneOffset.ofHours(8)).plusMinutes(6));
        //不需要attach，这些信息存储在缓存中。
        //wechatPayOrderCreateDTO.setAttach();
        String url = "";
        if(StringUtils.equals(launchPayDTO.getPayCreateDTO().getPayMode(), "qrcode")){
            url = wechatPayTrader.prepayPayByNative(wechatPayOrderCreateDTO);
        }

        return url;
    }

    public static void main(String[] args) {
        LocalDateTime parse = LocalDateTime.parse("2037-12-31 23:59:59",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(parse.toString());
    }

    @Override
    public PaymentCheckDTO paymentCheck(String userId, String queryId) {
        PaymentCheckDTO resp = new PaymentCheckDTO();
        resp.setStatus(PaymentCheckDTO.Status.UNKNOWN.getCode());
        PaymentPreCreateInfoDTO preCreateInfoCache = cacheUtil.getPreCreateInfoCache(queryId, PaymentPlatEnum.WECHAT_PAY.getCode());
        String orderId = queryId;
        if (null != preCreateInfoCache && null != preCreateInfoCache.getLaunchTimes()){
            queryId = queryId + "_" + preCreateInfoCache.getLaunchTimes();
        }
        Transaction transaction = wechatPayTrader.getTransactionByH5AndOrderId(queryId);
        if (transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)) {

            resendMsgService.sendWechatPayTradeSuccessMsg(transaction);

            resp.setStatus(PaymentCheckDTO.Status.TRADE_SUCCESS.getCode());
            PaymentCheckDTO.OrderInfo orderInfo = new PaymentCheckDTO.OrderInfo();
            orderInfo.setOrderId(orderId);

            if (Objects.nonNull(preCreateInfoCache)) {
                orderInfo.setCreateTime(preCreateInfoCache.getCreateTime());
            } else {
                ShareOrder shareOrder = orderManager.selByOrderId(orderId);
                if (Objects.isNull(shareOrder)) {
                    throw new CustomBusinessException(ErrorCodeEnum.ERROR_SERVER_ERROR);
                }
                orderInfo.setCreateTime(shareOrder.getCreateTime().getTime());
            }
            resp.setOrderInfo(orderInfo);
        }
        return resp;
    }

    @Override
    public boolean periodCheck(PaymentRegularCheckDTO paymentRegularCheckDTO, PaymentPreCreateInfoDTO precreateInfoCache) {
        Transaction transaction = wechatPayTrader.getTransactionByH5AndOrderId(paymentRegularCheckDTO.getQueryId());
        if (Objects.isNull(transaction) || !transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)) {
            return false;
        }
        resendMsgService.sendWechatPayTradeSuccessMsg(transaction);
        return true;
    }

}
