package upc.c505.modular.auth.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDateTime;

/**
 * @author sxz
 * @date 2023/7/30
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("区域筛选条件")
public class GetAreaPageParam extends PageBaseSearchParam {
    @ApiModelProperty("上级地区（父级0为最顶级）")
    private Long parentId;

    @ApiModelProperty("地区名称")
    private String areaName;

    @ApiModelProperty("显示排序")
    private Integer seq;

    @ApiModelProperty("状态(0:禁用，1：启用)")
    private Integer status;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
