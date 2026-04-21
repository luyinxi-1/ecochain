package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.supenterprise.entity.SupEnterprise;

/**
 * @Author: xth
 * @Date: 2024/10/21 16:11
 */
@Data
@Accessors(chain = true)
public class EcoChainGetEnterpriseBySocialCreditCodeReturnParam extends SupEnterprise {
    @ApiModelProperty("当前登录市场主体是否关联查询结果市场主体（1关联，0取消关联）")
    @TableField("status")
    private Integer status;
}
