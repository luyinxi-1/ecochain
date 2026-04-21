package upc.c505.modular.auth.entity;

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
 * 登录日志
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_login_log")
@ApiModel(value = "SysUserLoginLog对象", description = "登录日志")
public class SysUserLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户账号")
    @TableField("user_code")
    private String userCode;

    @ApiModelProperty("用户名称")
    @TableField("username")
    private String username;

    @ApiModelProperty("登录ip地址")
    @TableField("login_ip")
    private String loginIp;

    @ApiModelProperty("浏览器")
    @TableField("browser")
    private String browser;

    @ApiModelProperty("操作系统")
    @TableField("os_version")
    private String osVersion;

    @ApiModelProperty("登录状态(0:失败，1：成功)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("操作信息")
    @TableField("operational_information")
    private String operationalInformation;

    @ApiModelProperty("登录日期")
    @TableField("login_datetime")
    private LocalDateTime loginDatetime;

    @ApiModelProperty("创建人")
    @TableField(value = "creator")
    private String creator;

    @ApiModelProperty("创建时间")
    @TableField(value = "add_datetime")
    private LocalDateTime addDatetime;


}
