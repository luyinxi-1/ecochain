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
@TableName("eco_chain_regional_configuration")
@ApiModel(value = "EcoChainRegionalConfiguration对象", description = "")
public class EcoChainRegionalConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("区域组配置表")
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

    @ApiModelProperty("产业分组（地块名称、项目组、养殖区块、房间号、维修车间、加工区域）")
    @TableField("industry_group")
    private String industryGroup;

    @ApiModelProperty("地块负责人")
    @TableField("land_manager")
    private String landManager;

    @ApiModelProperty("联系电话")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty("创建人id")
    @TableField("creator_id")
    private Long creatorId;

    @ApiModelProperty("可见人（people_enterprise_id拼接）")
    @TableField("visible_people")
    private String visiblePeople;

    @ApiModelProperty("点位坐标（经纬度）")
    @TableField("point_coordinates")
    private String pointCoordinates;

    @ApiModelProperty("地址描述")
    @TableField("location")
    private String location;

    @ApiModelProperty("介绍")
    @TableField("introduction")
    private String introduction;

    @ApiModelProperty("照片")
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
