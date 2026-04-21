package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author: frd
 * @create-date: 2024/3/22 17:20
 */
@Data//lombok自动生成增删改查
@Accessors(chain = true)//链式编程
public class VideoConfigPageSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("模糊搜索参数")
    private String searchParam;

    @ApiModelProperty("监控属性（重点单位、重点项目、党组织会议室）")
    private String monitorAttributes;

    @ApiModelProperty("设备类型（海康、大华等）")
    private String deviceType;
}
