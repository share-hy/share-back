package com.share.hy.controller.user;

import com.share.hy.common.HttpCommonHeader;
import com.share.hy.common.ResponseMsg;
import com.share.hy.common.controller.BaseController;
import com.share.hy.service.IGoodsService;
import com.share.hy.service.IOrderService;
import com.share.hy.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/share/")
public class RecordController extends BaseController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IUserService userService;

    /**
     * 订单列表
     */
    @GetMapping("/order/record")
    public ResponseMsg<?> orderRecord() {
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        return success(orderService.queryOrder(httpCommonHeader.getUserId()));
    }

    /**
     * 服务变更列表
     */
    @GetMapping("/service/record")
    public ResponseMsg<?> serviceRecord() {
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        return success(goodsService.queryService(httpCommonHeader.getUserId()));
    }

    /**
     * 服务变更列表
     */
    @GetMapping("/balance/record")
    public ResponseMsg<?> balanceRecord() {
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        return success(userService.queryUserBalance(httpCommonHeader.getUserId()));
    }
}
