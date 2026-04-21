package upc.c505.modular.auth.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeptAreaParam {
    @ApiModelProperty(value = "部门Id")
    private Long deptId;
    @ApiModelProperty(value = "区域ID String ,分割")
    String areaIds ="";
}
