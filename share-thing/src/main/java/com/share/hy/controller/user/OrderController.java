package com.share.hy.controller.user;

import com.share.hy.common.CustomBusinessException;
import com.share.hy.common.ResponseMsg;
import com.share.hy.common.controller.BaseController;
import com.share.hy.dto.pay.OrderPreCreateResp;
import com.share.hy.dto.pay.PayCreateDTO;
import com.share.hy.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/share/Order")
@Slf4j
public class OrderController extends BaseController {

    @Autowired
    private IOrderService orderService;

    /**
     * 创建支付信息
     * @param payCreateDTO
     * @return
     */
    @PostMapping("/pay-create")
    public ResponseMsg<?> payCreate(@RequestBody PayCreateDTO payCreateDTO) {
        try {
            OrderPreCreateResp orderPreCreateResp = orderService.preCreateOrder(payCreateDTO);
            return success(orderPreCreateResp);
        } catch (CustomBusinessException e) {
            log.info("catch custom error",e);
            return failed(e.getErrorCodeEnumCommon());
        }
    }


}
