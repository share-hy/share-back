package com.share.hy.controller.admin;

import com.share.hy.common.ResponseMsg;
import com.share.hy.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/share/admin/")
public class AdminConfigController {

    @Autowired
    private IAdminService adminService;

    @GetMapping("vip/config/query")
    public ResponseMsg<?> vipConfigQuery(){
        return null;
    }

    @PostMapping("vip/config")
    public ResponseMsg<?> vipConfig(){
        return null;
    }

    @PostMapping("benefit/config")
    public ResponseMsg<?> benefitConfig(){
        return null;
    }

    @PostMapping("benefit/config/query")
    public ResponseMsg<?> benefitConfigQuery(){
        return null;
    }


}
