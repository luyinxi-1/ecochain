package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/7/8 9:56
 */

@Data
@Accessors(chain = true)
public class InspectionAnalysisReturnParam {
    @ApiModelProperty("许可证号")
    private String coaCode;

    @ApiModelProperty("违规信息")
    private String message;

    @ApiModelProperty("抽烟次数")
    private Integer smoking = 0;

    @ApiModelProperty("没戴安全帽")
    private Integer noHat = 0;

    @ApiModelProperty("发现飞虫")
    private Integer bug = 0;

    @ApiModelProperty("地面不整洁")
    private Integer untidyGround = 0;

    @ApiModelProperty("发现鼠患")
    private Integer mouseAppearing = 0;

    @ApiModelProperty("未戴口罩")
    private Integer noMask = 0;
}
