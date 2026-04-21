package upc.c505.modular.cascade.entity;

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
 * @author frd
 * @since 2024-07-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cascade_config")
@ApiModel(value = "CascadeConfig对象", description = "")
public class NewCascadeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("多级级联表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("1代表万善乡公共基础设施")
    @TableField("type")
    private String type;

    @ApiModelProperty("level为0的parent_id为0，	level为1的parent_id为父节点id，	level为2的parent_id为父节点Id")
    @TableField("parent_id")
    private Long parentId;

    @TableField("level")
    private Integer level;

    @ApiModelProperty("名字")
    @TableField("name")
    private String name;

    @TableField("status")
    private Integer status;

    @ApiModelProperty("标签颜色")
    @TableField("color")
    private String color;

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

    @ApiModelProperty("所属区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("所属区域名称")
    @TableField("area_name")
    private String areaName;


}
