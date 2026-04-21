package upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author: xth
 * @Date: 2025/4/16 16:29
 */
@Data
@Accessors(chain = true)
public class SelectWorkLogReturnParam {
    @ApiModelProperty("详细类型选择（种植品种、装饰类型、养殖品种、工作类型、维修类型、卤制种类）")
    private String detailTypeOption;

    @ApiModelProperty("产业分组选择（地块名称、项目组、养殖区块、房间号、维修车间、加工区域）")
    private String industryGroupOption;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("产业库名称（种植库名称、装饰库名称、养殖库名称、工作库名称、维修库名称、制作库名称）")
    private String industryWarehouse;

    @ApiModelProperty("状态(0未开始，1种植中，2采摘中，3完成，-1查询0/1/2三种状态)")
    private String status;

    @ApiModelProperty("记录时间")
    private LocalDateTime recordingTime;

    @ApiModelProperty("记录人")
    private String recorder;

    @ApiModelProperty("过程名称")
    private String processName;

    @ApiModelProperty("描述")
    private String recordDescribe;
    
    @ApiModelProperty("过程节点 ID")
    private Long ecoChainProcessNodeConfigurationId;
    
    @ApiModelProperty("查询的 id 类型")
    private String id;

    @ApiModelProperty("查询的 id 类型")
    private String type;

    @ApiModelProperty("记录所在库id")
    private Long warehouseId;
}
