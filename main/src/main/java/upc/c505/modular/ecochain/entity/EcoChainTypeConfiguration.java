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
 * @author xth
 * @since 2024-09-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_type_configuration")
@ApiModel(value = "EcoChainTypeConfiguration对象", description = "")
public class EcoChainTypeConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("品种类型配置")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("产业类型（种植产业、装修产业、牛羊肉、宾馆酒店、汽车维修）")
    @TableField("industry_type")
    private String industryType;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("详细类型（种植品种、装饰类型、养殖品种、工作类型、维修类型、卤制种类）")
    @TableField("detail_type")
    private String detailType;

    @ApiModelProperty("序号")
    @TableField("number")
    private Integer number;

    @ApiModelProperty("介绍")
    @TableField("introduction")
    private String introduction;

    @ApiModelProperty("品类照片")
    @TableField("pictures")
    private String pictures;

    @ApiModelProperty("区域id")
    @TableField(value = "area_id")
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