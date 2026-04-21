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
 * 权限表
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_auth")
@ApiModel(value = "SysAuth对象", description = "权限表")
public class SysAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("权限码")
    @TableField("auth_code")
    private String authCode;

    @ApiModelProperty("权限名称")
    @TableField("auth_name")
    private String authName;

    @ApiModelProperty("权限模块表id")
    @TableField("auth_model_id")
    private Long authModelId;

    @ApiModelProperty("权限模块名称")
    @TableField("auth_model_name")
    private String authModelName;

    @ApiModelProperty("顺序")
    @TableField("seq")
    private Integer seq;

    @ApiModelProperty("状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("权限类型（0：菜单，1：按件，2：其他）")
    @TableField("auth_type")
    private Integer authType;

    @ApiModelProperty("路由")
    @TableField("url")
    private String url;

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
