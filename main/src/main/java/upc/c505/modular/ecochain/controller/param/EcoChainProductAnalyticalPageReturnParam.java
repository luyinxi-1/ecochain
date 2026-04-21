package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: xth
 * @Date: 2024/9/30 10:56
 */
@Data
@Accessors(chain = true)
public class EcoChainProductAnalyticalPageReturnParam {
    @ApiModelProperty("产品id")
    @TableField("eco_chain_product_manager_id")
    private Long ecoChainProductManagerId;

    @ApiModelProperty("产品类型id")
    @TableField("eco_chain_product_classification_id")
    private String ecoChainProductClassificationId;

    @ApiModelProperty("产品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("产品分类名称")
    @TableField("product_classification_name")
    private String productClassificationName;

    @ApiModelProperty("产品扫码（浏览量）")
    private Long productScan;

    @ApiModelProperty("分享次数")
    private Long shareNumber;

    @ApiModelProperty("产品分享浏览次数")
    private Long productShareNumber;
}
