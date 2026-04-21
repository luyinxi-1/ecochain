package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import upc.c505.modular.ecochain.entity.EcoChainProcessTracking;

/**
 * @Author: xth
 * @Date: 2024/9/24 15:51
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class EcoChainBuildWarehouseReturnParam extends EcoChainBuildWarehouse {
    @ApiModelProperty(value = "周期(种植周期、装修周期...)")
    private Integer period;

    @ApiModelProperty(value = "采摘次数")
    private Long picking;

    @ApiModelProperty(value = "过程数据")
    private EcoChainProcessTracking ProcessData;

    @ApiModelProperty(value = "最新一条验收（完成）记录")
    private EcoChainCompleteRecord completeData;

    @ApiModelProperty(value = "过程跟踪数量")
    private Integer trackNumber;

    @ApiModelProperty(value = "完成记录数量")
    private Integer warehouseNumber;

}
