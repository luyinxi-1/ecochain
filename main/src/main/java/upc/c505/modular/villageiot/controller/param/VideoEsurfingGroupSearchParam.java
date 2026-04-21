package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/6/6 10:21
 */
@Data
@Accessors(chain = true)
public class VideoEsurfingGroupSearchParam {
    @ApiModelProperty("是否查询适用区域（0：查询管辖区域），1：查询适用区域")
    private Integer isApplicableArea;

    @ApiModelProperty("所属区域（适用区域）id")
    private Long areaId;

    @ApiModelProperty("查询标识")
    private Integer flag;

    @ApiModelProperty("区域id")
    private String regionId;
}
