package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.villageiot.entity.*;

import java.util.List;

/**
 * @author: frd
 * @create-date: 2024/1/19 15:30
 */
@Data
@Accessors(chain = true)
public class VideoConfigAddParam {
    @ApiModelProperty("视频配置的基本参数")
    VideoConfigCommon videoConfigCommon;

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
