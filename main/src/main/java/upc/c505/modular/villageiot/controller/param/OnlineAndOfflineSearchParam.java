package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author: frd
 * @create-date: 2024/7/4 10:36
 */
@Data
@Accessors(chain = true)
public class OnlineAndOfflineSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("监控属性（重点单位、重点项目、党组织会议室）")
    private String monitorAttributes;
}
