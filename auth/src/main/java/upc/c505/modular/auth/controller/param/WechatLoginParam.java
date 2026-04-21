package upc.c505.modular.auth.controller.param;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class WechatLoginParam {
    private String userCode;
    private String password;

    @NotNull(message = "openid不能为空")
    private String openid;
    private Integer userType;
    private String appId; // 小程序appId
}
