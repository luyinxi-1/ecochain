package upc.c505.modular.auth.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author sxz
 * @date 2023/9/25
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UserLoginLogSearchParam extends PageBaseSearchParam{
    @ApiModelProperty(value = "登录账号")
    private String usercode;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "登录ip")
    private String loginIp;
}
