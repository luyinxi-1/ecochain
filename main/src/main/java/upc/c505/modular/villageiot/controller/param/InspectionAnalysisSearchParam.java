package upc.c505.modular.villageiot.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/7/8 9:58
 */
@Data
@Accessors(chain = true)
public class InspectionAnalysisSearchParam {
    @ApiModelProperty("信用代码")
    private String entCreditCode;

    @ApiModelProperty("所属区域")
    private String areaId;
}
