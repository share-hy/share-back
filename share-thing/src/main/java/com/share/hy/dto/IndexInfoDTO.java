package com.share.hy.dto;

import com.share.hy.domain.ShareIndexConfig;
import lombok.Data;

@Data
public class IndexInfoDTO {

    private String desc;

    private String link;

    private Byte type;

    public IndexInfoDTO(ShareIndexConfig indexConfig) {
        this.desc = indexConfig.getDesc();
        this.link = indexConfig.getLink();
        this.type = indexConfig.getType();
    }
}
