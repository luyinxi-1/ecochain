package upc.c505.modular.ecochain.controller.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainAiSimulationReverseAssessmentReturnParam {
    @ApiModelProperty("人员姓名")
    private String name;

    @ApiModelProperty("过程跟踪数量")
    private Long processTrackingNumber;

    @ApiModelProperty("完成记录数量")
    private Long completedRecordsNumber;

    @ApiModelProperty("工作得分（过程跟踪数量*工作记录得分）")
    private Long workScore;

    @ApiModelProperty("绩效得分（完成记录数量*签单成交得分）")
    private Long performanceScore;

    @ApiModelProperty("基础分（公司给每个人员单独配置）")
    private Long basicScore;

    @ApiModelProperty("总计得分（工作得分、绩效得分、基础分之和）")
    private Long totalScore;

    @ApiModelProperty("k值（基础工资÷总计得分）")
    private Double kValue;

    @ApiModelProperty("排名（根据K值从小到大排序，1,2,3,4,5这样排）")
    private Integer rank;
}
