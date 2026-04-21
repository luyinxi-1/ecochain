package upc.c505.modular.auth.entity;

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
 * 区域表
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_area")
@ApiModel(value = "SysArea对象", description = "区域表")
public class SysArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("系统_区域表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("上级地区（父级0为最顶级）")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty("区域类型（1区县,2乡镇街道,3社区,4村/小区/网格）")
    @TableField("area_type")
    private Integer areaType;

    @ApiModelProperty("地区名称")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty("地图等级（1区县,2乡镇街道,3社区,4村/小区/网格）")
    @TableField("level")
    private String level;

    @ApiModelProperty("显示排序")
    @TableField("seq")
    private Integer seq;

    @ApiModelProperty("经度")
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private String latitude;

    @ApiModelProperty("状态(0:禁用，1：启用)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("该区域是否有默认角色（0 否，1是）")
    @TableField("flag")
    private Integer flag;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

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
