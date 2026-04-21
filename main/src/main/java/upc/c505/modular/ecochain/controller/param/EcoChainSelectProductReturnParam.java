package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import upc.c505.modular.ecochain.entity.EcoChainProductManager;

import java.util.List;

@Data
public class EcoChainSelectProductReturnParam {

    @ApiModelProperty("产品分类表")
    private Long id;

    @ApiModelProperty("产品分类名称")
    private String productClassificationName;

    @ApiModelProperty("产品下的分类")
    private List<EcoChainProductManager> children;
}
