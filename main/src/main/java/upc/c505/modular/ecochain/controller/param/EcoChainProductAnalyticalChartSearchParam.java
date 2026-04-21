package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @Author: xth
 * @Date: 2024/9/30 10:52
 */
@Data
@Accessors(chain = true)
public class EcoChainProductAnalyticalChartSearchParam  extends PageBaseSearchParam {
    @ApiModelProperty(value = "开始时间，格式为yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty(value = "结束时间，格式为yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @ApiModelProperty("产品id（统计类型非产品扫码或者分享时此字段为空）")
    @TableField("eco_chain_product_manager_id")
    private Long ecoChainProductManagerId;
}
