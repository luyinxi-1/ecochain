package upc.c505.modular.auth.controller.param.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author sxz
 * @date 2023/8/31
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserPageSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("适用区域id")
    private Long areaId;

    @ApiModelProperty("适用区域名称")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty("用户类型（-1管理员，-2二级管理员，1企业用户，2政府人员，3网格人员，4居民用户，5政府人员和网格人员）")
    @TableField("user_type")
    private Integer userType;

    @ApiModelProperty("用户名称")
    @TableField("username")
    private String username;

    @ApiModelProperty("登录账号")
    @TableField("user_code")
    private String userCode;

    @ApiModelProperty("状态(0停用，1启用)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("模糊搜索参数")
    private String searchParam;

    @ApiModelProperty("企业用户类型（-1建设单位，-2监理单位， 1新型农业经营主体， 2九小场所， 3普通市场主体， 4物业公司）")
    private Integer enterpriseUserType;
}
