package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainAiSimulationReverseAssessmentUpdateParam {
//    @ApiModelProperty(value = "从业人员id", required = true)
//    private Long id;

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "基础分", required = true)
    private Long basicScore;

    @ApiModelProperty(value = "基础工资", required = true)
    private Long basicSalary;
}