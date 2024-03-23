package com.share.hy.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFinishMsgDTO {

    @NotBlank(message = "orderId must not blank")
    private String orderId;

    @NotBlank(message = "userId must not blank")
    private String userId;

    @NotBlank(message = "subjectId must not blank")
    private String subjectId;
}
