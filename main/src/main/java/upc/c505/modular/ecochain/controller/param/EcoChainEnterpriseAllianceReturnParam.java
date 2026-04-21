package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAlliance;

/**
 * @Author: xth
 * @Date: 2024/9/25 15:49
 */
@Data
@Accessors(chain = true)
public class EcoChainEnterpriseAllianceReturnParam extends EcoChainEnterpriseAlliance {
    @ApiModelProperty(value = "联盟模式(1:本公司关联其他公司，2:其他公司关联本公司)")
    private Integer AllianceMode;
}
