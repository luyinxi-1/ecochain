package upc.c505.common;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * 存进redis的用户信息
 * 存进UserUtils
 * 
 * @author sxz
 */
@Data
@Accessors(chain = true)
public class UserInfoToRedis {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("登录账号")
    private String userCode;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户类型（-1管理员，-2二级管理员，1企业用户，2政府人员，3网格人员，4居民用户）")
    @TableField("user_type")
    private Integer userType;

    @ApiModelProperty("适用区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("适用区域名称")
    @TableField("area_name")
    private String areaName;

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

    @ApiModelProperty("美丽乡村建设者表id")
    @TableField("village_construction_builder_id")
    private Long villageConstructionBuilderId;

    @ApiModelProperty("美丽乡村建设单位表id")
    @TableField("village_construction_unit_id")
    private Long villageConstructionUnitId;

    @ApiModelProperty("市场主体表id")
    @TableField("sup_enterprise_id")
    private Long supEnterpriseId;

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

    @ApiModelProperty("角色列表code")
    private List<String> roleCodeList;

    @ApiModelProperty("管辖区域权限List")
    private List<Long> manageAreaIdList;

    @ApiModelProperty("企业用户类型（-1建设单位，-2监理单位， 1新型农业经营主体， 2九小场所， 3普通市场主体， 4物业公司）")
    @TableField("enterprise_user_type")
    private Integer enterpriseUserType;
    /**
     * 小程序登录添加的内容
     */
    @ApiModelProperty("密码")
    private String password;
    private String token;
    @ApiModelProperty("小程序appId")
    private String appId;
}
