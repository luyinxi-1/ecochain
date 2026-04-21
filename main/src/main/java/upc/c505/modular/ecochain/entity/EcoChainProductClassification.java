package upc.c505.modular.ecochain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
@TableName("eco_chain_product_classification")
@ApiModel(value = "EcoChainProductClassification对象", description = "")
public class EcoChainProductClassification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品分类表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("产品分类名称")
    @TableField("product_classification_name")
    private String productClassificationName;

    @ApiModelProperty("产品排序序号")
    @TableField("sort_number")
    private Integer sortNumber;

    @ApiModelProperty("父id（上一级的产品分类id）")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty("产品分类等级(1表示一级分类，2表示二级分类，3表示三级分类)")
    @TableField("classification_grade")
    private Integer classificationGrade;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("Logo")
    @TableField("logo")
    private String logo;

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

    @TableField(exist = false)
    private List<EcoChainProductClassification> children;

}
