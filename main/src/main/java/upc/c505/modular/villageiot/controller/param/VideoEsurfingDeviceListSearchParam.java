package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/7/10 16:49
 */
@Data
@Accessors(chain = true)
public class VideoEsurfingDeviceListSearchParam {
    @ApiModelProperty("是否查询适用区域（0：查询管辖区域），1：查询适用区域")
    private Integer isApplicableArea;

    @ApiModelProperty("所属区域（适用区域）id")
    private Long areaId;

    @ApiModelProperty("查询标识")
    private Integer flag;

    @ApiModelProperty("区域id")
    private String regionId;

    @ApiModelProperty("页码，默认 1")
    private Integer pageNo;

    @ApiModelProperty("分页大小，默认 10，最大值 50")
    private Integer pageSize;
}
