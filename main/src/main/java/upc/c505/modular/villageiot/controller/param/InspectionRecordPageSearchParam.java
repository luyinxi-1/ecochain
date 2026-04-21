package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDateTime;

/**
 * @author: frd
 * @create-date: 2024/7/6 9:18
 */
@Data
@Accessors(chain = true)
public class InspectionRecordPageSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("监控属性")
    private String monitorAttributes;

    @ApiModelProperty("企业名称（项目名称）")
    private String enterpriseName;

    @ApiModelProperty("信用代码（项目编号）")
    private String entCreditCode;

    @ApiModelProperty("巡查处理人（检查记录人）")
    private String handle;

    @ApiModelProperty("起始检查时间")
    private LocalDateTime startInspectionTime;

    @ApiModelProperty("终止检查时间")
    private LocalDateTime endInspectionTime;

    @ApiModelProperty("是否答复（0否，1是），整改状态")
    private Integer isReply;
}
