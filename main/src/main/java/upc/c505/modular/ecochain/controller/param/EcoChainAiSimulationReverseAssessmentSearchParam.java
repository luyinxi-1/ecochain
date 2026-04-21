package upc.c505.modular.ecochain.controller.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainAiSimulationReverseAssessmentSearchParam {
    @ApiModelProperty(value = "开始时间，格式为yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty(value = "结束时间，格式为yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @ApiModelProperty("人员姓名")
    private String name;

    @ApiModelProperty("排序（0：按k值从小到大；1：按k值从大到小）")
    private Integer sortFlag;
}
