package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: frd
 * @create-date: 2024/3/22 11:12
 */
@Data
@Accessors(chain = true)
public class VideoConfigDeleteParam {

    @ApiModelProperty(value = "视频配置id")
    private Integer id;

    @ApiModelProperty("摄像头id列表")
    private List<Integer> pointIds;
}
