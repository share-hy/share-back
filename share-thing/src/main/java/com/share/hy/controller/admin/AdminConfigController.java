package com.share.hy.controller.admin;

import com.share.hy.common.HttpCommonHeader;
import com.share.hy.common.ResponseMsg;
import com.share.hy.common.controller.BaseController;
import com.share.hy.dto.admin.BenefitInfoDTO;
import com.share.hy.dto.admin.VipInfoDTO;
import com.share.hy.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/share/admin/")
public class AdminConfigController extends BaseController {

    @Autowired
    private IAdminService adminService;

    @GetMapping("vip/config/query")
    public ResponseMsg<?> vipConfigQuery(){
        return success(adminService.vipQuery());
    }

    @PostMapping("vip/config")
    public ResponseMsg<?> vipConfig(@RequestBody @Valid List<VipInfoDTO> vipInfoDTOList){
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        adminService.vipConfig(vipInfoDTOList,httpCommonHeader.getUserId());
        return success();
    }

    @PostMapping("benefit/config")
    public ResponseMsg<?> benefitConfig(@RequestBody @Valid List<BenefitInfoDTO> list){
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        adminService.benefitConfig(list,httpCommonHeader.getUserId());
        return success();
    }

    @PostMapping("benefit/config/query")
    public ResponseMsg<?> benefitConfigQuery(){
        return success(adminService.benefitQuery());
    }


}
