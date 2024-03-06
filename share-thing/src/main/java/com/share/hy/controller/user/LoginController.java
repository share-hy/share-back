package com.share.hy.controller.user;

import com.share.hy.common.ResponseMsg;
import com.share.hy.common.controller.BaseController;
import com.share.hy.dto.user.UserAuthDTO;
import com.share.hy.dto.user.UserLoginDTO;
import com.share.hy.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/share/user")
public class LoginController extends BaseController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseMsg<?> login(@RequestBody @Valid UserLoginDTO userLoginDTO){
        return success(userService.userLogin(userLoginDTO));
    }

    @PostMapping("/register")
    public ResponseMsg<?> register(@RequestBody @Valid UserLoginDTO userRegister){
        return success(userService.register(userRegister));
    }

    @PostMapping("/logout")
    public ResponseMsg<?> logout(@RequestBody @Valid UserLoginDTO userLoginDTO){
        return success(userService.userLogin(userLoginDTO));
    }
}
