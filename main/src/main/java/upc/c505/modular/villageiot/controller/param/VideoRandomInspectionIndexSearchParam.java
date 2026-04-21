package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author: frd
 * @create-date: 2024/3/25 10:03
 */
@Data
public class VideoRandomInspectionIndexSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("视频配置表id")
    private Integer id;

    @ApiModelProperty("监控属性（重点单位、重点项目、党组织会议室）")
    private String monitorAttributes;

    @ApiModelProperty("监控组名称（企业名称）")
    private String monitorGroupName;

    @ApiModelProperty("许可类别（许可证类型）")
    private String coaType;

    @ApiModelProperty("许可证号")
    private String coaCode;

    @ApiModelProperty("社会信用代码/项目编号/党组织id")
    private String entCreditCode;

}
