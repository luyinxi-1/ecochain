package upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EcoChainWorkStatisticsSignContractRateReturnParam {
    @ApiModelProperty("人员名字")
    private String name;

    @ApiModelProperty("跟踪项目数量")
    private Integer trackingNum;

    @ApiModelProperty("验收记录数量")
    private Integer completeRecordNum;

    @ApiModelProperty("签单率")
    private Double rate;
}
