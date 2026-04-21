package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.villageiot.entity.*;

import java.util.List;

/**
 * @author: frd
 * @create-date: 2024/6/21 10:16
 */
@Data
@Accessors(chain = true)
public class VideoConfigReturnParam {

    @ApiModelProperty("视频配置")
    private VideoConfigCommon videoConfigCommon;

    @ApiModelProperty("摄像头列表")
    List<Object> videoList;
}
