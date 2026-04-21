package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: 批量修改库的可见人请求参数
 * @Author: Assistant
 * @Date: 2026/3/18
 */
@Data
@Accessors(chain = true)
public class EcoChainBuildWarehouseVisiblePeopleUpdateParam {

    @ApiModelProperty("库 IDs")
    private List<Long> warehouseIds;

    @ApiModelProperty("可见人 IDs（people_enterprise_id）")
    private List<Long> visiblePeopleIds;
}
