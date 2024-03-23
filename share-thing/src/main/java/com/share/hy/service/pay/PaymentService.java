package com.share.hy.service.pay;


import com.share.hy.common.enums.PaymentPlatEnum;
import com.share.hy.dto.pay.LaunchPayDTO;
import com.share.hy.dto.pay.PaymentCheckDTO;
import com.share.hy.dto.pay.PaymentPreCreateInfoDTO;
import com.share.hy.dto.pay.PaymentRegularCheckDTO;

public interface PaymentService {
    PaymentPlatEnum getTradePlat();

    /**
     * 发起支付
     */
    String launchPay(LaunchPayDTO launchPayDTO);


    PaymentCheckDTO paymentCheck(String userId, String queryId);


    boolean periodCheck(PaymentRegularCheckDTO paymentRegularCheckDTO, PaymentPreCreateInfoDTO preCreateInfoCache);
}
