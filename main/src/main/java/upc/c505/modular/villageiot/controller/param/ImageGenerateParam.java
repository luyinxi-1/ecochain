package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/6/29 15:51
 */
@Data
@Accessors(chain = true)
public class ImageGenerateParam {

    @ApiModelProperty("事项名称")
    private String item;

    @ApiModelProperty("拍摄时间")
    private String dateTime;

    @ApiModelProperty("拍摄地点")
    private String location;

    @ApiModelProperty("经纬度")
    private String latitudeAndLongitude;

    @ApiModelProperty("拍摄人")
    private String photographer;
}
