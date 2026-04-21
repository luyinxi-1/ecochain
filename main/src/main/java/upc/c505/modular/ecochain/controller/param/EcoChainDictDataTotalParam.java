package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.ecochain.entity.EcoChainDictData;

import java.util.List;
@Data
public class EcoChainDictDataTotalParam {
    @ApiModelProperty("总数")
    private Integer totalNum;

    @ApiModelProperty("字段数据列表")
    private List<EcoChainDictData> EcoChainDictDataList;
}
