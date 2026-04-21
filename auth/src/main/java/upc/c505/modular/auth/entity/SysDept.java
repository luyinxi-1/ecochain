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
 * 系统_部门表
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dept")
@ApiModel(value = "SysDept对象", description = "系统_部门表")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("系统_部门表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("适用区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("适用区域名称")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty("部门code")
    @TableField("dept_code")
    private String deptCode;

    @ApiModelProperty("部门名称")
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty("上级部门id")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty("部门层级（0为最高层）")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("部门在当前层级下的顺序，由小到大")
    @TableField("seq")
    private Integer seq;

    @ApiModelProperty("默认角色id")
    @TableField("default_role_id")
    private Long defaultRoleId;

    @ApiModelProperty("默认角色名称")
    @TableField("default_role_name")
    private String defaultRoleName;

    @ApiModelProperty("部门类型")
    @TableField("dept_type")
    private String deptType;

    @ApiModelProperty("部门负责人")
    @TableField("leader_name")
    private String leaderName;

    @ApiModelProperty("联系电话")
    @TableField("contact_phone")
    private String contactPhone;

    @ApiModelProperty("部门职责、人员及简介")
    @TableField("introduction")
    private String introduction;

    @ApiModelProperty("部门照片")
    @TableField("picture")
    private String picture;

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
