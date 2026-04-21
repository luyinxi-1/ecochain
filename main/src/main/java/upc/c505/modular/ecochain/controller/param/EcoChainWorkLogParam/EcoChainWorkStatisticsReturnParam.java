package upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EcoChainWorkStatisticsReturnParam {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("过程跟踪数量")
    private Long processTrackingNum;

    @ApiModelProperty("完成记录数量")
    private Long completeRecordNum;

    @ApiModelProperty("总数")
    private Long sumNum;
}
