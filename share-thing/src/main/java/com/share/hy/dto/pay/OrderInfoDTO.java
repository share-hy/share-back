package com.share.hy.dto.pay;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.share.hy.common.enums.DurationEnum;
import com.share.hy.domain.ShareGoodsItem;
import com.share.hy.domain.ShareOrder;
import com.share.hy.domain.ShareUserTradeRecord;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderInfoDTO {

    private BigDecimal paymentAmount;

    private Long createTime;

    private String content;

    private BigDecimal balanceUsed;

    private String thirdPayId;

    /**
     * {@link com.share.hy.common.enums.OrderStatusEnum}
     */
    private Byte status;

    public OrderInfoDTO(ShareOrder order, ShareGoodsItem goodsItem,String goodsName, List<ShareUserTradeRecord> userTradeRecords) {
        this.createTime = order.getCreateTime().getTime();
        this.status = order.getStatus();
        this.paymentAmount = order.getPaymentAmount();
        if (StringUtils.isNotBlank(goodsName)){
            this.content = goodsName + " +" + DurationEnum.getDayByDuration(goodsItem.getDuration()) + "天";
        }
        this.thirdPayId = order.getTradeId();
    }
}
