package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDateTime;

/**
 * @Description: 企业授权表分页查询类
 * @Author: mjh
 * @CreateTime: 2024-09-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EcoChainEnterpriseAuthorizePageSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("授权表")
    private Long id;

    @ApiModelProperty("企业社会信用代码")
    private String creditCode;

    @ApiModelProperty("模糊查询参数 企业名称")
    private String supEnterpriseName;

    @ApiModelProperty("授权到期日期")
    private LocalDateTime endDate;

    @ApiModelProperty("业务经理")
    private String managerName;

    @ApiModelProperty("业务经理手机号")
    private String managerPhone;

    @ApiModelProperty("存储容量（单位G）")
    private Double storageCapacity;

    @ApiModelProperty("添加人")
    private String addName;

    @ApiModelProperty("添加日期")
    private LocalDateTime addDate;

    @ApiModelProperty("授权状态（0待授权、1正常、2试用）")
    private Integer authorizeStatus;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("适用区域id")
    private Long areaId;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private LocalDateTime addDatetime;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationDatetime;
}
