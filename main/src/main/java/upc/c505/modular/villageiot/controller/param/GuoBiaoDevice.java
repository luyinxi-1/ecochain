package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: frd
 * @create-date: 2024/6/24 11:02
 */

@Data
public class GuoBiaoDevice {

    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("信道id")
    private String channelId;
}
