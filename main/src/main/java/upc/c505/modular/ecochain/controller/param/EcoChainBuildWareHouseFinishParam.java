package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class EcoChainBuildWareHouseFinishParam {

    @ApiModelProperty("种植库id")
    @TableField("eco_chain_build_warehouse_id")
    private Long ecoChainBuildWarehouseId;

    @ApiModelProperty("市场定价")
    @TableField("market_pricing")
    private String marketPricing;

    @ApiModelProperty("质保期")
    @TableField("warranty_period")
    private String warrantyPeriod;

    @ApiModelProperty("其他描述")
    @TableField("other_description")
    private String otherDescription;
    // 仅用于返回周期，不保存到数据库
    @ApiModelProperty("周期，计算方式为 allcompletiondatetime - addDatetime")
    private Long cycle;
}
