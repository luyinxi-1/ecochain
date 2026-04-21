package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.ecochain.entity.EcoChainExtendedTable;
@Data
@Accessors(chain = true)
public class EcoChainExtendedTableUpdateParam extends EcoChainExtendedTable {

    @ApiModelProperty("地址前缀")
    private String addressPrefix;

}
