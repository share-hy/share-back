package com.share.hy.manager.impl;

import com.share.hy.domain.ShareIndexConfig;
import com.share.hy.manager.IndexManager;
import com.share.hy.mapper.ShareIndexConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Component
public class IndexManagerImpl implements IndexManager {

    @Autowired
    private ShareIndexConfigMapper shareIndexConfigMapper;


    @Override
    public List<ShareIndexConfig> queryByType(Byte type) {
        Example example = new Example(ShareIndexConfig.class);
        example.orderBy("sort");
        example.createCriteria().andEqualTo("type",type)
                .andEqualTo("enable",1);
        return shareIndexConfigMapper.selectByExample(example);
    }
}
