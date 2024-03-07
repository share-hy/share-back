package com.share.hy.controller;

import com.share.hy.common.ResponseMsg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/share/index")
public class IndexController {

    @GetMapping
    public ResponseMsg<?> downloadInfo(){

    }

}
