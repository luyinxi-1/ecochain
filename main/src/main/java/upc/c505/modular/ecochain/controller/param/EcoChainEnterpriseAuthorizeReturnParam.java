package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAuthorize;

/**
 * @Description: 授权请求查询返回类
 * @Author: mjh
 * @CreateTime: 2024-09-25
 */
@Data
@Accessors(chain = true)
public class EcoChainEnterpriseAuthorizeReturnParam extends EcoChainEnterpriseAuthorize {

    @ApiModelProperty("市场主体名称")
    private String supEnterpriseName;

    @ApiModelProperty("联络人")
    private String contactName;

    @ApiModelProperty("联络人电话")
    private String contactPhone;

    @ApiModelProperty("已使用容量")
    private Double usedSize;

    @ApiModelProperty("用户状态：0正常、1容量报警、2授权预警、3已超期、4容量报警&授权预警、5容量报警&已超期")
    private Integer userStatus;
}
