package upc.c505.modular.ecochain.controller.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.util.Map;

/**
 * @Description: 分析概览模块搜索参数
 * @Author: la
 * @CreateTime: 2024-09-27
 */

@Data
@Accessors(chain = true)
public class EcoChainAnalyticalOverviewSearchParam extends PageBaseSearchParam{

    @ApiModelProperty(value = "开始时间，格式为yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty(value = "结束时间，格式为yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @ApiModelProperty(value = "社会信用代码")
    private String socialCreditCode;
}
