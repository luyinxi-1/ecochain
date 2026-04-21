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
 * @author mjh
 * @since 2024-09-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_enterprise_authorize")
@ApiModel(value = "EcoChainEnterpriseAuthorize对象", description = "")
public class EcoChainEnterpriseAuthorize implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("授权表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("企业社会信用代码")
    @TableField("credit_code")
    private String creditCode;

    @ApiModelProperty("授权到期日期")
    @TableField("end_date")
    private LocalDateTime endDate;

    @ApiModelProperty("业务经理")
    @TableField("manager_name")
    private String managerName;

    @ApiModelProperty("业务经理手机号")
    @TableField("manager_phone")
    private String managerPhone;

    @ApiModelProperty("存储容量（单位G）")
    @TableField("storage_capacity")
    private Double storageCapacity;

    @ApiModelProperty("添加人")
    @TableField("add_name")
    private String addName;

    @ApiModelProperty("添加日期")
    @TableField("add_date")
    private LocalDateTime addDate;

    @ApiModelProperty("授权状态（0待授权、1正常、2试用、3高级会员、4VIP会员、5高级VIP会员）")
    @TableField("authorize_status")
    private Integer authorizeStatus;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("适用区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("缴费日期")
    @TableField("payment_date")
    private LocalDateTime paymentDate;

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
