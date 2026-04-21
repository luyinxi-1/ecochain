package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainWorkLogGetByIdParam {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("类型(0：过程跟踪  1：完成记录)")
    private Integer type;

}
