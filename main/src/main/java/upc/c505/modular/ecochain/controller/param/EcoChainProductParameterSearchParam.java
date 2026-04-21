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
public class EcoChainProductParameterSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("产品参数名称")
    @TableField("parameter_name")
    private String parameterName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("状态（0表示停用，1表示启用")
    private String status;

    @ApiModelProperty("类型")
    @TableField("type")
    private String type;
}
