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
 * @since 2024-09-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_dict_type")
@ApiModel(value = "EcoChainDictType对象", description = "")
public class EcoChainDictType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("字典主键")
    @TableId(value = "dict_type_id", type = IdType.AUTO)
    private Integer dictTypeId;

    @ApiModelProperty("字典名称")
    @TableField("dict_name")
    private String dictName;

    @ApiModelProperty("字典类型")
    @TableField("dict_type")
    private String dictType;

    @ApiModelProperty("适用区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("状态（0正常 1停用）")
    @TableField("status")
    private String status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("添加人")
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("添加时间")
    @TableField(value = "add_date_time", fill = FieldFill.INSERT)
    private LocalDateTime addDateTime;

    @ApiModelProperty("修改人")
    @TableField(value = "operator", fill = FieldFill.UPDATE)
    private String operator;

    @ApiModelProperty("修改时间")
    @TableField(value = "operation_date_time", fill = FieldFill.UPDATE)
    private LocalDateTime operationDateTime;


}
