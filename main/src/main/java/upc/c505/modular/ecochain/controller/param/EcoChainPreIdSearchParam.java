package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EcoChainPreIdSearchParam {
    @ApiModelProperty(value = "用户的openid", required = true)
    private String openId;

    @ApiModelProperty(value = "总金额(以分为单位)", required = true)
    private Integer total;

    @ApiModelProperty("缴费日期")
    private LocalDateTime paymentDate;

    @ApiModelProperty("授权状态（0待授权、1正常、2试用、3高级会员、4VIP会员、5高级VIP会员）")
    private Integer authorizeStatus;

    @ApiModelProperty("授权到期日期")
    private LocalDateTime endDate;

    @ApiModelProperty("存储容量（单位G）")
    private Double storageCapacity;
}
