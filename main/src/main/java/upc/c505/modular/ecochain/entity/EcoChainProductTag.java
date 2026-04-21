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
@TableName("eco_chain_product_tag")
@ApiModel(value = "EcoChainProductTag对象", description = "")
public class EcoChainProductTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品标签表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("产品标签名称")
    @TableField("tag_name")
    private String tagName;

    @ApiModelProperty("状态（0停用，1启用）")
    @TableField("status")
    private String status;

    @ApiModelProperty("排序序号")
    @TableField("sort_number")
    private Integer sortNumber;

    @ApiModelProperty("颜色")
    @TableField("color")
    private String color;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("是否为默认值（1：默认值，默认值不可删除）")
    @TableField("is_default")
    private String isDefault;

    @ApiModelProperty("类型")
    @TableField("type")
    private String type;

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
