package upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EcoChainWorkStatisticsSearchParam {

    @NotNull
    @ApiModelProperty("开始时间")
    private String startTime;

    @NotNull
    @ApiModelProperty("结束时间")
    private String endTime;

    @NotNull
    @ApiModelProperty("查询类型(1-记录人姓名 2-类型 3-区域 4-项目名称)")
    private Integer searchType;

    @ApiModelProperty("查询内容")
    private String name;

    @ApiModelProperty("导出类型（0：工作统计， 1：签单率）")
    private Integer type = 0;
}
