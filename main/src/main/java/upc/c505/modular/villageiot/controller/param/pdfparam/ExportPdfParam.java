package upc.c505.modular.villageiot.controller.param.pdfparam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/7/13 9:43
 */
@Data
@Accessors(chain = true)
public class ExportPdfParam {
    @ApiModelProperty(value = "企业名称")
    private String enterpriseName;

    @ApiModelProperty(value = "信用代码")
    private String entCreditCode;

    @ApiModelProperty(value = "经营地址")
    private String businessAddress;

    @ApiModelProperty(value = "负责人")
    private String principal;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "巡查记录")
    private String record;

    @ApiModelProperty(value = "巡查时间")
    private String inspectionTime;

    @ApiModelProperty(value = "处理意见")
    private String handlingOpinions;

    @ApiModelProperty(value = "巡查处理人")
    private String handle;

    @ApiModelProperty(value = "处理时间")
    private String handleTime;

    private PicTurePdf picTurePdf;
}
