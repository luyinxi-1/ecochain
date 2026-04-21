package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zdz
 */
@Data
@Accessors(chain = true)
public class CountRemoteSupervisionReturnParam {
    @ApiModelProperty("项目编号")
    private String projectNumber;

    @ApiModelProperty("远程督导数量")
    private Integer remoteSupervisionNumber;
}
