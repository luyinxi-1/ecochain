package upc.c505.modular.auth.controller.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author sxz
 */
@Data
@ApiModel("角色属性")
public class RoleParam {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色code")
    @NotEmpty(message = "code不能为空")
    private String roleCode;

    @ApiModelProperty("角色名称")
    @NotEmpty(message = "名称不能为空")
    private String roleName;

    @ApiModelProperty("角色顺序")
    @NotNull(message = "角色顺序不能为空")
    private Integer seq;

    @ApiModelProperty("状态")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private LocalDateTime addDatetime;
}
