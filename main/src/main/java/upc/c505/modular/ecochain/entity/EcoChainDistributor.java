package upc.c505.modular.ecochain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @author byh
 * @since 2024-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_distributor")
@ApiModel(value = "EcoChainDistributor对象", description = "")
public class EcoChainDistributor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("生态链分销商表")
    @TableId("id")
    private Long id;

    @ApiModelProperty("分享人电话")
    @TableField("sharer_phone")
    private String sharerPhone;

    @ApiModelProperty("分享人姓名")
    @TableField("sharer_name")
    private String sharerName;

    @ApiModelProperty("分享人信用代码")
    @TableField("sharer_social_credit_code")
    private String sharerSocialCreditCode;

    @ApiModelProperty("注册人电话")
    @TableField("registrant_phone")
    private String registrantPhone;

    @ApiModelProperty("注册人姓名")
    @TableField("registrant_name")
    private String registrantName;

    @ApiModelProperty("注册人信用代码")
    @TableField("registrant_social_credit_code")
    private String registrantSocialCreditCode;

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
    @TableField(value = "operation_datetime", fill = FieldFill.UPDATE)
    private LocalDateTime operationDatetime;


}
