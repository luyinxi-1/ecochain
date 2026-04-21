package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EcoChainDistributorPageReturnParam {

    @ApiModelProperty("授权状态（0待授权、1正常、2试用、3高级会员、4VIP会员、5高级VIP会员）")
    private Integer authorizeStatus;

    @ApiModelProperty("用户状态(0正常、1容量报警、2授权报警、3已超期、4容量报警&授权预警)")
    private Integer userStatus;

    @ApiModelProperty("授权到期日期")
    private LocalDateTime endDate;

    @ApiModelProperty("分享人电话")
    private String sharerPhone;

    @ApiModelProperty("分享人姓名")
    private String sharerName;

    @ApiModelProperty("分享人信用代码")
    private String sharerSocialCreditCode;

    @ApiModelProperty("注册人电话")
    private String registrantPhone;

    @ApiModelProperty("注册人姓名")
    private String registrantName;

    @ApiModelProperty("注册人信用代码")
    private String registrantSocialCreditCode;

    @ApiModelProperty("缴费日期")
    private LocalDateTime paymentDate;
}
