package upc.c505.modular.ecochain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("eco_chain_build_warehouse")
@ApiModel(value = "EcoChainBuildWarehouse对象", description = "")
public class EcoChainBuildWarehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("生态链建库")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("批号")
    @TableField("batch_number")
    private String batchNumber;

    @ApiModelProperty("详细类型选择（品种类型配置id）")
    @TableField("eco_chain_type_configuration_id")
    private Long ecoChainTypeConfigurationId;

    @ApiModelProperty("详细类型选择（种植品种、装饰类型、养殖品种、工作类型、维修类型、卤制种类）")
    @TableField("detail_type_option")
    private String detailTypeOption;

    @ApiModelProperty("产业分组选择（区域组配置表id）")
    @TableField("eco_chain_regional_configuration_id")
    private Long ecoChainRegionalConfigurationId;

    @ApiModelProperty("产业分组选择（地块名称、项目组、养殖区块、房间号、维修车间、加工区域）")
    @TableField("industry_group_option")
    private String industryGroupOption;

    @ApiModelProperty("产业库名称（种植库名称、装饰库名称、养殖库名称、工作库名称、维修库名称、制作库名称）")
    @TableField("industry_warehouse")
    private String industryWarehouse;

    @ApiModelProperty("产业库联系人")
    @TableField("warahouse_contact")
    private String warahouseContact;

    @ApiModelProperty("产业库联系电话")
    @TableField("warahouse_phone")
    private String warahousePhone;

    @ApiModelProperty("（种植、装饰、养殖、房间、车间、加工区域）数量")
    @TableField("quantity")
    private String quantity;

    @ApiModelProperty("备注")
    @TableField("notes")
    private String notes;

    @ApiModelProperty("建库照片")
    @TableField("warahouse_pictures")
    private String warahousePictures;

    @ApiModelProperty("创建人id")
    @TableField("creator_id")
    private Long creatorId;

    @ApiModelProperty("可见人姓名")
    @TableField("visible_people_name")
    private String visiblePeopleName;

    @ApiModelProperty("可见人（people_enterprise_id拼接）")
    @TableField("visible_people")
    private String visiblePeople;

    @ApiModelProperty("状态(0未开始，1种植中，2采摘中，3完成，-1查询0/1/2三种状态)")
    @TableField("status")
    private String status;

    @ApiModelProperty("市场定价")
    @TableField("market_pricing")
    private String marketPricing;

    @ApiModelProperty("质保期")
    @TableField("warranty_period")
    private String warrantyPeriod;

    @ApiModelProperty("其他描述")
    @TableField("other_description")
    private String otherDescription;

    @ApiModelProperty("区域id")
    @TableField(value = "area_id")
    private Long areaId;

    @ApiModelProperty("是否作为案例（1作为案例0不作为案例，默认为0）")
    @TableField(value = "is_case")
    private Integer isCase;

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
    @ApiModelProperty("全部完工日期")
    @TableField(value = "all_completion_datetime", fill = FieldFill.UPDATE)
    private LocalDateTime allCompletionDatetime;

}
