package upc.c505.modular.auth.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author sxz
 * @date 2023/7/30
 **/
@Data
@ApiModel("添加/修改区域参数")
public class AreaParam {

    @ApiModelProperty("区域id（添加时不填，修改时填）")
    private Long id;

    @ApiModelProperty("上级地区（父级0为最顶级）")
    @TableField("parent_id")
    @NotNull(message = "上级地区id不能为空")
    private Long parentId;

    @ApiModelProperty("区域类型（1区县,2乡镇街道,3社区,4村/小区/网格）")
    @TableField("area_type")
    @NotNull(message = "区域类型不能为空")
    private Integer areaType;

    @ApiModelProperty("地区名称")
    @TableField("area_name")
    @NotNull(message = "地区名称不能为空")
    private String areaName;

    @ApiModelProperty("显示排序")
    @TableField("seq")
    @NotNull(message = "显示排序不能为空")
    private Integer seq;

    @ApiModelProperty("经度")
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private String latitude;

    @ApiModelProperty("状态(0:禁用，1：启用)")
    @TableField("status")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("地图等级（1区县,2乡镇街道,3社区,4村/小区/网格）")
    @TableField("level")
    private String level;
}
