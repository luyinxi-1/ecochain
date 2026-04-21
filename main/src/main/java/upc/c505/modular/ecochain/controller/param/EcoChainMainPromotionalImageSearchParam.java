package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EcoChainMainPromotionalImageSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("照片名称")
    @TableField("picture_name")
    private String pictureName;

    @ApiModelProperty("状态（0表示停用，1表示启用）")
    @TableField("status")
    private String status;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;
}
