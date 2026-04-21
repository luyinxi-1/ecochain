package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @Author: xth
 * @Date: 2024/9/25 15:48
 */
@Data
@Accessors(chain = true)
public class EcoChainEnterpriseAlliancePageSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("公司名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("联盟企业名称")
    @TableField("alliance_enterprise_name")
    private String allianceEnterpriseName;

    @ApiModelProperty("联盟企业社会信用代码")
    @TableField("alliance_social_credit_code")
    private String allianceSocialCreditCode;

    @ApiModelProperty("是否关联（1关联，0取消关联）")
    @TableField("status")
    private Integer status;
}
