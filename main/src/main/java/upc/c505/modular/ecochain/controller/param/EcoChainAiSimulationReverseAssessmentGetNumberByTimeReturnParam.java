package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.ecochain.service.IEcoChainWorkLogService;

@Data
@Accessors(chain = true)
public class EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam {
    @ApiModelProperty("天/月份")
    private String time;

    @ApiModelProperty("过程跟踪数")
    private Integer processTrackingNumber = 0;

    @ApiModelProperty("完成记录数")
    private Integer CompleteRecordNumber = 0;
}
