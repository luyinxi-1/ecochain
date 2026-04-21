package upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author: xth
 * @Date: 2025/4/16 20:40
 */
@Data
@Accessors(chain = true)
public class ExportWorkLogReturnParam {
    @ApiModelProperty("详细类型选择（种植品种、装饰类型、养殖品种、工作类型、维修类型、卤制种类）")
    private String detailTypeOption;

    @ApiModelProperty("产业分组选择（地块名称、项目组、养殖区块、房间号、维修车间、加工区域）")
    private String industryGroupOption;

    @ApiModelProperty("项目名称")
    private String industryWarehouse;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("状态(0未开始，1种植中，2采摘中，3完成，-1查询0/1/2三种状态)")
    private String status;

    @ApiModelProperty("记录时间")
    private LocalDateTime recordingTime;

    @ApiModelProperty("记录人")
    private String recorder;

    @ApiModelProperty("过程节点名称")
    private String ecoChainProcessNodeConfigurationName;

    @ApiModelProperty("过程节点id")
    private Long ecoChainProcessNodeConfigurationId;

    @ApiModelProperty("过程名称")
    private String processName;

    @ApiModelProperty("描述")
    private String recordDescribe;

    @ApiModelProperty("位置")
    private String location;

    @ApiModelProperty("评价人")
    private String evaluator;

    @ApiModelProperty("评价时间")
    private LocalDateTime evaluationDate;

    @ApiModelProperty("评价结果")
    private String evaluateResults;

    @ApiModelProperty("照片")
    private String pictures;

    @ApiModelProperty("id(用于过滤无效数据)")
    @ExcelProperty("id")
    private String id;

}
