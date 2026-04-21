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
 * @author byh
 * @since 2024-09-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_enterprise_alliance")
@ApiModel(value = "EcoChainEnterpriseAlliance对象", description = "")
public class EcoChainEnterpriseAlliance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("合作联盟表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    @ApiModelProperty("区域id")
    @TableField("area_id")
    private Long areaId;

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
