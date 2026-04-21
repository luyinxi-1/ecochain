package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.villageiot.entity.*;

import java.util.List;

/**
 * @author: frd
 * @create-date: 2024/3/22 10:56
 */
@Data
@Accessors(chain = true)
public class VideoConfigUpdateParam {

    @ApiModelProperty(value = "视频配置id")
    private Integer id;

    @ApiModelProperty(value = "设备类型（海康、大华等）")
    private String deviceType;

    @ApiModelProperty("海康的设备列表")
    List<VideoConfigHaikangisc> haikangiscList;

    @ApiModelProperty("大华的设备列表")
    List<VideoConfigDahua7016> dahua7016List;

    @ApiModelProperty("天翼的设备列表")
    List<VideoConfigEsurfing> esurfingList;

    @ApiModelProperty("国标的设备列表")
    List<VideoConfigGuobiao> guobiaoList;

    @ApiModelProperty("内蒙的设备列表")
    List<VideoConfigIoa> ioaList;

    @ApiModelProperty("宇视的设备列表")
    List<VideoConfigUniview> univiewList;

    @ApiModelProperty("紫光的设备列表")
    List<VideoConfigZiguang> ziguangList;

    @ApiModelProperty("其他类型的设备列表")
    List<VideoConfigOther> otherList;

}
