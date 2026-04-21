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
 * 用户表
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("登录账号")
    @TableField("user_code")
    private String userCode;

    @ApiModelProperty("用户名称")
    @TableField("username")
    private String username;

    @ApiModelProperty("密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("用户类型（-1管理员，-2二级管理员，1企业用户，2政府人员，3网格人员，4居民用户，5企业人员）")
    @TableField("user_type")
    private Integer userType;

    @ApiModelProperty("适用区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("适用区域名称")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty("管辖区域id(网格员)")
    @TableField("manage_area_id")
    private Long manageAreaId;

    @ApiModelProperty("管辖区域名称")
    @TableField("manage_area_name")
    private String manageAreaName;

    @ApiModelProperty("企业人员表id")
    @TableField("people_enterprise_id")
    private Long peopleEnterpriseId;

    @ApiModelProperty("政府人员表id")
    @TableField("people_goverment_id")
    private Long peopleGovermentId;

    @ApiModelProperty("网格人员表id")
    @TableField("people_grider_id")
    private Long peopleGriderId;

    @ApiModelProperty("居民用户表id")
    @TableField("people_resident_id")
    private Long peopleResidentId;

    @ApiModelProperty("市场主体表id")
    @TableField("sup_enterprise_id")
    private Long supEnterpriseId;

    @ApiModelProperty("美丽乡村建设者表id")
    @TableField("village_construction_builder_id")
    private Long villageConstructionBuilderId;

    @ApiModelProperty("美丽乡村建设单位表id")
    @TableField("village_construction_unit_id")
    private Long villageConstructionUnitId;

    @ApiModelProperty("职务表的id")
    @TableField("job_id")
    private Long jobId;

    @ApiModelProperty("职务名称")
    @TableField("job_name")
    private String jobName;

    @ApiModelProperty("部门id")
    @TableField("dept_id")
    private Long deptId;

    @ApiModelProperty("部门名称")
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty("小程序openid")
    @TableField("openid")
    private String openid;

    @ApiModelProperty("状态(0停用，1启用)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("绑定人员信息，用于前端显示")
    @TableField("ent_name")
    private String entName;

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

    @ApiModelProperty("企业用户类型（-1建设单位，-2监理单位， 1新型农业经营主体， 2九小场所， 3普通市场主体， 4物业公司）")
    @TableField("enterprise_user_type")
    private Integer enterpriseUserType;

    @ApiModelProperty("小程序appId")
    @TableField("app_id")
    private String appId;

}
