package com.share.hy.controller.admin;

import com.share.hy.common.ResponseMsg;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/share/admin/")
public class VipController {

    @PostMapping("vip/query")
    public ResponseMsg<?> vipConfigQuery(){

    }

    @PostMapping("vip/config")
    public ResponseMsg<?> vipConfigQuery(){

    }
}
