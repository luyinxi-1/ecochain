package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EcoChainCollectSearchParam {
    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("名称")
    private String name;
}
