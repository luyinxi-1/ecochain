package upc.c505.modular.auth.controller.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 用户登录账号密码
 * @author sxz
 */
@Data
@ApiModel("用户登录账号密码")
public class UserLoginParam {

    @ApiModelProperty("账号")
    @NotEmpty(message = "用户账号不能为空")
    private String userCode;

    @ApiModelProperty("密码")
    @NotEmpty(message = "用户密码不能为空")
    private String password;

}
