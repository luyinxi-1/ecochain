package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: xth
 * @Date: 2024/9/23 15:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EcoChainProcessNodeConfigurationPageSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("节点名称（种植节点名称、装饰节点名称、养殖节点名称、管护节点名称、维修节点名称、制作节点名称）")
    @TableField("node_name")
    private String nodeName;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;
}
