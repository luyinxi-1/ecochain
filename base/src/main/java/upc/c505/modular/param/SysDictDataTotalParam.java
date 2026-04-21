package upc.c505.modular.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import upc.c505.modular.dict.entity.SysDictData;

import java.util.List;

@Data
public class SysDictDataTotalParam {
    @ApiModelProperty("总数")
    private Integer totalNum;

    @ApiModelProperty("字段数据列表")
    private List<SysDictData> sysDictDataList;
}
