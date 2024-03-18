package com.share.hy.controller.user;

import com.share.hy.common.ResponseMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/share/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建支付信息
     * @param orderCreatReqDTO
     * @return
     */
    @PostMapping("/pay-create")
    public ResponseMsg<?> payCreate(@RequestBody PaymentOrderCreatReqDTO orderCreatReqDTO) {
        try {
            OrderPreCreateResp orderPreCreateResp = orderService.preCreateOrder(orderCreatReqDTO);
            return success(orderPreCreateResp);
        } catch (CustomBusinessException e) {
            log.info("catch custom error",e);
            return failed(e.getErrorCodeEnumCommon());
        }
    }


}
