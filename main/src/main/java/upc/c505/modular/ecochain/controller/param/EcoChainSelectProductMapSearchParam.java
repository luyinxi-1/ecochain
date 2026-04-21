package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import upc.c505.common.requestparam.PageBaseSearchParam;

@Data
public class EcoChainSelectProductMapSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("产品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("类型")
    @TableField("type")
    private String type;
}
