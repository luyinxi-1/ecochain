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
 * 职务表
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_job")
@ApiModel(value = "SysJob对象", description = "职务表")
public class SysJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("系统_职务表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("适用区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("适用区域名称")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty("职务code")
    @TableField("job_code")
    private String jobCode;

    @ApiModelProperty("职务名称")
    @TableField("job_name")
    private String jobName;

    @ApiModelProperty("是否为主要职务（0否，1是）")
    @TableField("is_main")
    private Integer isMain;

    @ApiModelProperty("排序号")
    @TableField("seq")
    private Integer seq;

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
