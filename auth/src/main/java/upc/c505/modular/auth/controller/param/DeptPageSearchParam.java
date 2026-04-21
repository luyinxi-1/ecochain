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
@ApiModel("部门筛选条件")
public class DeptPageSearchParam extends PageBaseSearchParam {
//    @ApiModelProperty("适用区域id")
//    private Long areaId;

    @ApiModelProperty("部门类型")
    private String deptType;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门id")
    private Long id;
}
