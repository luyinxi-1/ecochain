package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainAiSimulationReverseAssessmentGetNumberByTimeParam {
    @ApiModelProperty("人员姓名")
    private String peopleName;

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("月份")
    private String month;
}
