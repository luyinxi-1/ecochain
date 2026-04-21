package upc.c505.modular.auth.controller.param;

/**
 * @author sxz
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateUserPasswordParam {

    @ApiModelProperty("账号")
    @NotEmpty(message = "用户账号不能为空")
    private String userCode;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty("旧密码")
    @NotEmpty(message = "旧密码不能为空")
    private String oldPassword;

}
