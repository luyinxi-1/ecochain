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
public class EcoChainProductClassificationSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("产品分类名称")
    @TableField("product_classification_name")
    private String productClassificationName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("类型")
    @TableField("type")
    private String type;
}
