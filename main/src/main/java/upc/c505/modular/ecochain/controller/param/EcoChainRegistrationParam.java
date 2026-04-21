package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EcoChainRegistrationParam {
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
    @NotNull(message = "注册人信用代码不能为空")
    private String registrantSocialCreditCode;

    @ApiModelProperty("注册人身份证号")
    private String registerIdentityNumber;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("住所/经营场所")
    private String businessPlace;

    @ApiModelProperty("法人姓名")
    private String legalRepresentative;

    @ApiModelProperty("行业门类")
    private String industryType;

    @ApiModelProperty("企业用户类型（1新型农业经营主体， 2九小场所， 3普通市场主体，4物业公司）")
    @NotNull(message = "企业用户类型不能为空")
    private Integer enterpriseType;

    @ApiModelProperty("营业期限自")
    private LocalDate obtermStartDate;

    @ApiModelProperty("是否添加分销商表")
    private Boolean isAddDistributor;

    @ApiModelProperty("openid")
    @NotNull(message = "openid不能为空")
    private String openid;

    @ApiModelProperty("小程序appId")
    private String appId;
}
