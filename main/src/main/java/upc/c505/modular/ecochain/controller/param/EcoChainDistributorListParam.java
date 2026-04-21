package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import upc.c505.modular.ecochain.entity.EcoChainDistributor;

import java.time.LocalDateTime;

@Data
public class EcoChainDistributorListParam extends EcoChainDistributor {

    @ApiModelProperty("授权到期日期")
    private LocalDateTime endDate;

    @ApiModelProperty("存储容量（单位G）")
    private Double storageCapacity;

    @ApiModelProperty("授权状态（0待授权、1正常、2试用）")
    private Integer authorizeStatus;

    @ApiModelProperty("缴费日期")
    private LocalDateTime paymentDate;

    @ApiModelProperty("用户状态")
    private Integer userStatus;

    @ApiModelProperty("适用区域id")
    private Long areaId;

}
