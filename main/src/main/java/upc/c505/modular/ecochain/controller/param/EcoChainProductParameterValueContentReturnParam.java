package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.ecochain.entity.EcoChainProductParameterValueContent;

import java.util.List;

@Data
@Accessors(chain = true)
public class EcoChainProductParameterValueContentReturnParam {

    @ApiModelProperty("产品参数值内容总数")
    private Integer total;

    @ApiModelProperty("产品参数值内容")
    private List<EcoChainProductParameterValueContent> valueList;
}
