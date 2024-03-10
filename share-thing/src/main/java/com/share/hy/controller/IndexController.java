package com.share.hy.controller;

import com.share.hy.common.ResponseMsg;
import com.share.hy.common.controller.BaseController;
import com.share.hy.domain.ShareIndexConfig;
import com.share.hy.dto.IndexInfoDTO;
import com.share.hy.manager.IndexManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/share/index")
public class IndexController extends BaseController {

    @Autowired
    private IndexManager indexManager;

    @GetMapping("/link/query/{type}")
    public ResponseMsg<?> queryLinkByType(@PathVariable Byte type){
        List<ShareIndexConfig> configs = indexManager.queryByType(type);
        return success(CollectionUtils.isNotEmpty(configs) ? simpleInfo(configs) : Collections.emptyList());
    }

    private List<IndexInfoDTO> simpleInfo(List<ShareIndexConfig> configs) {
        return configs.stream().map(IndexInfoDTO::new).collect(Collectors.toList());
    }

}
