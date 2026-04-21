package upc.c505.modular.auth.controller.param.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import javax.validation.constraints.NotEmpty;

/**
 * @author sxz
 * @date 2023/8/22
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GetRolePageParam extends PageBaseSearchParam {

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("角色code")
    @NotEmpty(message = "code不能为空")
    private String roleCode;

    @ApiModelProperty("角色名称")
    @NotEmpty(message = "名称不能为空")
    private String roleName;

    @ApiModelProperty("适用区域id")
    private Long areaId;

    @ApiModelProperty("适用区域name")
    private String areaName;

    @ApiModelProperty("是否为默认角色(0否，1是)")
    private Integer isDefaultRole;

    @ApiModelProperty("角色顺序")
    @NotEmpty(message = "角色顺序不能为空")
    private Integer seq;

    @ApiModelProperty("状态")
    @NotEmpty(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;
}
