package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/6/24 9:11
 */

@Data
@Accessors(chain = true)
public class VideoInspectionPointLocationInfoParam {

    @ApiModelProperty("监控配置子表id")
    private Integer deviceId;

    @ApiModelProperty("设备类型（海康、大华等）")
    private String deviceType;

    @ApiModelProperty("监控点名称")
    private String name;

    @ApiModelProperty("取流地址")
    private String url;

    @ApiModelProperty("在线状态，0离线，1在线")
    private Integer online;

    @ApiModelProperty("播放器类型")
    private String videoSign;

}
