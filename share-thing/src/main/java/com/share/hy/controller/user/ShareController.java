package com.share.hy.controller.user;

import com.share.hy.common.HttpCommonHeader;
import com.share.hy.common.ResponseMsg;
import com.share.hy.common.controller.BaseController;
import com.share.hy.service.IShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/share/")
public class ShareController extends BaseController {

    @Autowired
    private IShareService shareService;

    /**
     * 查看邀请链接
     */
    @GetMapping("/invite/link")
    public ResponseMsg<?> inviteLink() {
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        return success(shareService.inviteLink(httpCommonHeader.getUserId()));
    }

    @GetMapping("/income/overview")
    public ResponseMsg<?> incomeOverView() {
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        return success(shareService.earningsOverview(httpCommonHeader.getUserId()));
    }
    @GetMapping("/income/detail")
    public ResponseMsg<?> incomeOverView(@RequestParam Byte level,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(defaultValue = "1") Integer pageNum) {
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        return success(shareService.incomeDetail(httpCommonHeader.getUserId(),level,pageSize,pageNum));
    }

    @GetMapping("/subordinate/overview")
    public ResponseMsg<?> subordinateOverView() {
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        return success(shareService.subordinateOverview(httpCommonHeader.getUserId()));
    }
}
