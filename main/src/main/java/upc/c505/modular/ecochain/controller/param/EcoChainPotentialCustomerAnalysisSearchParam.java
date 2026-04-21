package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class EcoChainPotentialCustomerAnalysisSearchParam {
    @ApiModelProperty("潜在客户分析")
    private Long id;

    @ApiModelProperty("微信头像")
    private String wechatAvatar;

    @ApiModelProperty("微信名")
    private String wechatName;

    @ApiModelProperty("联系电话")
    private String phoneNumber;

    @ApiModelProperty("浏览次数（新增时不填，默认为1）")
    private Integer viewsNumber;

    @ApiModelProperty("小程序openid")
    private String openid;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private LocalDateTime addDatetime;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty("社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty("升降序")
    private Integer isAsc;
}
