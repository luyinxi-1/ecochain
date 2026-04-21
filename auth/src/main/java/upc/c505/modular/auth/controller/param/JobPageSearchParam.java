package upc.c505.modular.auth.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author sxz
 * @date 2023/8/31
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("职务筛选条件")
public class JobPageSearchParam extends PageBaseSearchParam {

//    @ApiModelProperty("区域id")
//    private String areaId;

    @ApiModelProperty("职务名称")
    private String jobName;

    @ApiModelProperty("是否为主要职务（0否，1是）")
    private Integer isMain;
}
