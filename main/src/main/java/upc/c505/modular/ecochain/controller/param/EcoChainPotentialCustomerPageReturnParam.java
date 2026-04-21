package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainPotentialCustomerPageReturnParam {
    @ApiModelProperty("微信头像")
    private String wechatAvatar;

    @ApiModelProperty("微信名")
    private String wechatName;

    @ApiModelProperty("联系电话")
    private String phoneNumber;

    @ApiModelProperty("浏览次数")
    private Long total;

    @ApiModelProperty("社会信用代码")
    private String socialCreditCode;

}
