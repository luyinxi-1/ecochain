package upc.c505.modular.ecochain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author la
 * @since 2024-11-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_potential_customer_analysis")
@ApiModel(value = "EcoChainPotentialCustomerAnalysis对象", description = "")
public class EcoChainPotentialCustomerAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("潜在客户分析")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("微信头像")
    @TableField("wechat_avatar")
    private String wechatAvatar;

    @ApiModelProperty("微信名")
    @TableField("wechat_name")
    private String wechatName;

    @ApiModelProperty("联系电话")
    @TableField("phone_number")
    private String phoneNumber;

    @ApiModelProperty("浏览次数（新增时不填，默认为1）")
    @TableField("views_number")
    private Integer viewsNumber;

    @ApiModelProperty("小程序openid")
    @TableField("openid")
    private String openid;

    @ApiModelProperty("创建人")
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("创建时间")
    @TableField(value = "add_datetime", fill = FieldFill.INSERT)
    private LocalDateTime addDatetime;

    @ApiModelProperty("操作人")
    @TableField(value = "operator", fill = FieldFill.UPDATE)
    private String operator;

    @ApiModelProperty("操作时间")
    @TableField("operation_time")
    private LocalDateTime operationTime;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;
}
