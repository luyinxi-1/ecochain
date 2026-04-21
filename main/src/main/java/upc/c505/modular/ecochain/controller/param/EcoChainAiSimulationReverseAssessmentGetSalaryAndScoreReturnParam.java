package upc.c505.modular.ecochain.controller.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam {
    @ApiModelProperty("AI模拟反向测评表id")
    private Long id;

    @ApiModelProperty("人员姓名")
    private String name;

    @ApiModelProperty("基础分（公司给每个人员单独配置）")
    private Long basicScore;

    @ApiModelProperty("基础工资")
    private Long basicSalary;
}
