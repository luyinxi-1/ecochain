package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xth
 * @Date: 2024/9/23 20:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EcoChainBuildWareHousePageSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("产业库名称（种植库名称、装饰库名称、养殖库名称、工作库名称、维修库名称、制作库名称）")
    @TableField("industry_warehouse")
    private String industryWarehouse;

    @ApiModelProperty("详细类型选择（种植品种、装饰类型、养殖品种、工作类型、维修类型、卤制种类）")
    @TableField("detail_type_option")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> detailTypeOption;

    @ApiModelProperty("产业分组选择（地块名称、项目组、养殖区块、房间号、维修车间、加工区域）")
    @TableField("industry_group_option")
    private String industryGroupOption;

    @ApiModelProperty("可见人姓名")
    @TableField("visible_people_name")
    private String visiblePeopleName;

    @ApiModelProperty("状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("批号")
    @TableField("batch_number")
    private String batchNumber;

    @ApiModelProperty("案例")
    private Integer isCase;

    @ApiModelProperty("过程id")
    private Long processId;

    @ApiModelProperty("过程名称")
    private String processName;

    @ApiModelProperty("最新过程节点开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestProcessStartTime;

    @ApiModelProperty("最新过程节点结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestProcessEndTime;

    @ApiModelProperty("验收记录名称（模糊搜索 completeRecord，取最新一条验收记录匹配）")
    private String completeRecord;
}
