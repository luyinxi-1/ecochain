package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class EcoChainWorkLogGetByIdReturnParam {

    @ApiModelProperty("记录人")
    private String recorder;

    @ApiModelProperty("记录时间")
    private LocalDateTime recordingTime;

    @ApiModelProperty("位置")
    private String location;

    @ApiModelProperty("过程名称")
    private String processName;

    @ApiModelProperty("过程节点名称（采摘：灌溉，施肥）")
    private Long ecoChainProcessNodeConfigurationId;

    @ApiModelProperty("图片")
    private String pictures;

    @ApiModelProperty("评价人")
    private String evaluator;

    @ApiModelProperty("评价结果")
    private String evaluateResults;

    @ApiModelProperty("评价时间")
    private LocalDateTime evaluationDate;

    @ApiModelProperty("描述")
    private String recordDescribe;

}
