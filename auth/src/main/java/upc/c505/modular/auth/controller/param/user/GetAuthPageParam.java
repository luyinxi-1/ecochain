package upc.c505.modular.auth.controller.param.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author sxz
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("权限分页")
public class GetAuthPageParam extends PageBaseSearchParam {
    @ApiModelProperty("权限模块表id")
    @TableField("auth_model_id")
    private Long authModelId;

    @ApiModelProperty("权限模块名称")
    @TableField("auth_model_name")
    private Long authModelName;

    @ApiModelProperty("权限名称")
    private String authName;

    @ApiModelProperty("顺序")
    private Integer seq;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("权限类型（0：菜单，1：按件，2：其他）")
    private Integer authType;

    @ApiModelProperty("路由")
    private String url;

}
