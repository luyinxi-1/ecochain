package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/3/21 16:45
 */
@Data
@Accessors(chain = true)
public class VideoUnitReturnParam {
    @ApiModelProperty("视频单位id")
    private Long videoUnitId;

    @ApiModelProperty("视频单位名称（1、重点单位：单位名称2、重点项目：项目名称3、党组织会议：党组织名称）")
    private String videoUnitName;

    @ApiModelProperty("视频单位编号（1、重点单位：社会信用代码2、重点项目：项目编号3、党组织会议：无）")
    private String videoUnitNumber;

    @ApiModelProperty("单位/项目/党组织的areaId")
    private Long areaId;
}
