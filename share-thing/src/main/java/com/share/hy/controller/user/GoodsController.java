package com.share.hy.controller.user;

import com.share.hy.common.HttpCommonHeader;
import com.share.hy.common.ResponseMsg;
import com.share.hy.common.controller.BaseController;
import com.share.hy.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/share/goods/")
public class GoodsController extends BaseController {

    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询套餐内容
     */
    @GetMapping("query")
    public ResponseMsg<?> goodsQuery(){
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        return success(goodsService.queryByUserId(httpCommonHeader.getUserId()));
    }

    /**
     * 套餐支付详情
     */
    @GetMapping("detail")
    public ResponseMsg<?> detail(@RequestParam String goodsItemId){
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        return success(goodsService.detail(httpCommonHeader.getUserId(),goodsItemId));
    }

}
