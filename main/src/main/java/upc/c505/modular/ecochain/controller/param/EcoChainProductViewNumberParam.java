package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainProductViewNumberParam {

    @ApiModelProperty("产品id")
    private Long id;

    @ApiModelProperty("浏览量")
    private Long viewNumber = 0L;
}
