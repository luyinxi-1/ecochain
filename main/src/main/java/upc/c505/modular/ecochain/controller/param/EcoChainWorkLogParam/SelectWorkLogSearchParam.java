package upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xth
 * @Date: 2025/4/16 16:22
 */
@Data
@Accessors(chain = true)
public class SelectWorkLogSearchParam  extends PageBaseSearchParam {
    @ApiModelProperty("社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty("记录人")
    private String recorder;

    @ApiModelProperty("记录时间开始")
    private String recordingTimeStart;

    @ApiModelProperty("记录时间结束")
    private String recordingTimeEnd;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("详细类型选择（种植品种、装饰类型、养殖品种、工作类型、维修类型、卤制种类）")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> detailTypeOption;

    @ApiModelProperty("产业分组选择（地块名称、项目组、养殖区块、房间号、维修车间、加工区域）")
    private String industryGroupOption;

    @ApiModelProperty("产业库名称（种植库名称、装饰库名称、养殖库名称、工作库名称、维修库名称、制作库名称）")
    private String industryWarehouse;

    @ApiModelProperty("状态(0未开始，1种植中，2采摘中，3完成，-1查询0/1/2三种状态)")
    @TableField("status")
    private String status;

    @ApiModelProperty("查询的id类型")
    private String type;

    @ApiModelProperty("项目id")
    private Long warehouseId;
}
