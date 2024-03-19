package com.share.hy.service;


import com.share.hy.common.enums.PaymentPlatEnum;
import com.share.hy.dto.pay.PaymentCheckDTO;

public interface PaymentService {
    PaymentPlatEnum getTradePlat();

    /**
     * 发起支付
     */
    String launchPay(LaunchPayDTO launchPayDTO);


    PaymentCheckDTO paymentCheck(String userId, String queryId);


    boolean periodCheck(PaymentRegularCheckDTO paymentRegularCheckDTO, PaymentPreCreateInfoDTO preCreateInfoCache);
}
