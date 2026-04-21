package upc.c505.modular.dict.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author chenchen
 * @since 2022-06-16
 */
@Getter
@Setter
@TableName("sys_dict_data")
@ApiModel(value = "SysDictData对象", description = "字典数据表")
public class SysDictData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("字典编码")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("字典键值")
    @TableField(value = "dict_key")
    private String dictKey;

    @ApiModelProperty("字典排序")
    @TableField("dict_sort")
    private Integer dictSort;

    @ApiModelProperty("字典标签(一般根据name查，中文不支持情况下根据id查)")
    @TableField("name")
    private String name;

    @ApiModelProperty("字典类型")
    @TableField("dict_type")
    private String dictType;

    @ApiModelProperty("是否默认（Y是 N否）")
    @TableField("is_default")
    private String isDefault;

    @ApiModelProperty("状态（0正常 1停用）")
    @TableField("status")
    private String status;

    @ApiModelProperty("颜色")
    @TableField("color")
    private String color;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("适用区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("添加人")
    @TableField(value = "creator",fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("添加时间")
    @TableField(value = "add_date_time",fill = FieldFill.INSERT)
    private LocalDateTime addDateTime;

    @ApiModelProperty("更新者")
    @TableField(value = "operator",fill = FieldFill.UPDATE)
    private String operator;

    @ApiModelProperty("修改时间")
    @TableField(value = "operation_date_time",fill = FieldFill.UPDATE)
    private LocalDateTime operationDateTime;


}
