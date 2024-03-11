package com.share.hy.controller.user;

import com.share.hy.common.HttpCommonHeader;
import com.share.hy.common.ResponseMsg;
import com.share.hy.common.controller.BaseController;
import com.share.hy.common.enums.ErrorCodeEnum;
import com.share.hy.dto.user.UserAuthDTO;
import com.share.hy.dto.user.UserLoginDTO;
import com.share.hy.service.IUserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/share/")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    @PostMapping("user/login")
    public ResponseMsg<?> login(@RequestBody @Valid UserLoginDTO userLoginDTO){
        return success(userService.userLogin(userLoginDTO));
    }

    @PostMapping("user/register")
    public ResponseMsg<?> register(@RequestBody @Valid UserLoginDTO userRegister){
        return success(userService.register(userRegister));
    }

    @GetMapping("user/logout")
    public ResponseMsg<?> logout(){
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        userService.logout(httpCommonHeader.getUserId());
        return success();
    }

    @GetMapping("account/info")
    public ResponseMsg<?> info(){
        HttpCommonHeader httpCommonHeader = getHttpCommonHeader();
        String userId = httpCommonHeader.getUserId();
        if (StringUtils.isBlank(userId)){
            return failed(ErrorCodeEnum.ERROR_PARAM_WRONG);
        }
        return success(userService.accountInfo(userId));
    }


}
