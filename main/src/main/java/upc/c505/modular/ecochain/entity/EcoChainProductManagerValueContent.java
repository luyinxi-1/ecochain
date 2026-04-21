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
@TableName("eco_chain_product_manager_value_content")
@ApiModel(value = "EcoChainProductManagerValueContent对象", description = "")
public class EcoChainProductManagerValueContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品管理-值内容关联表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("产品id")
    @TableField("eco_chain_product_manager_id")
    private Long ecoChainProductManagerId;

    @ApiModelProperty("产品参数id")
    @TableField("eco_chain_product_parameter_id")
    private Long ecoChainProductParameterId;

    @ApiModelProperty("产品参数值内容id")
    @TableField("eco_chain_product_manager_value_content_id")
    private String ecoChainProductParameterValueContentId;

    @ApiModelProperty("参数值内容")
    @TableField("value_content")
    private String valueContent;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

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
