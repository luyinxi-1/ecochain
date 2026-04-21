package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainProductManager;

/**
 * @Author: xth
 * @Date: 2024/9/27 15:43
 */
@Data
@Accessors(chain = true)
public class EcoChainProductManagerPageSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("产品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("状态（0停用，1启用）")
    @TableField("status")
    private String status;

    @ApiModelProperty("产品标签id")
    @TableField("eco_chain_product_tag_id")
    private Long ecoChainProductTagId;

    @ApiModelProperty("置顶（0不置顶，1置顶）")
    @TableField("top_up")
    private String topUp;

    @ApiModelProperty("产品分类id")
    @TableField("eco_chain_product_classification_id")
    private Long ecoChainProductClassificationId;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("类型")
    @TableField("type")
    private String type;
}
