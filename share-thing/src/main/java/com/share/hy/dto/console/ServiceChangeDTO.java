package com.share.hy.dto.console;

import com.share.hy.common.enums.DurationEnum;
import com.share.hy.common.enums.GoodsStatusEnum;
import com.share.hy.domain.ShareGoodsItem;
import com.share.hy.domain.ShareServiceRecord;
import com.share.hy.manager.GoodsManager;
import lombok.Data;

@Data
public class ServiceChangeDTO {

    private Long changeTime;

    private String type;

    private String content;

    public ServiceChangeDTO(ShareServiceRecord shareServiceRecord, GoodsManager goodsManager) {
        this.changeTime = shareServiceRecord.getCreateTime().getTime();
        this.type = GoodsStatusEnum.getDescByCode(shareServiceRecord.getServiceType());
        ShareGoodsItem goodsItem = goodsManager.queryByGoodsItemId(shareServiceRecord.getGoodsItemId());
        int days = DurationEnum.getDayByDuration(goodsItem.getDuration());
        String goodsName = goodsManager.getGoodsName(shareServiceRecord.getGoodsItemId());
        this.content = goodsName + " +" + days +"天";
    }
}
