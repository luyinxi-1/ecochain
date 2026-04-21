package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

@Data
@Accessors(chain = true)
public class EcoChainDictDataPageSearchParam extends PageBaseSearchParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("字典编码")
    private Integer id;

    @ApiModelProperty("字典键值")
    private Integer dict_key;

    @ApiModelProperty("字典排序")
    private Integer dictSort;

    @ApiModelProperty("字典标签(一般根据name查，中文不支持情况下根据id查)")
    private String name;

    @ApiModelProperty("字典类型")
    private String dictType;

    @ApiModelProperty("是否默认（Y是 N否）")
    private String isDefault;

    @ApiModelProperty("状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("备注")
    private String remark;
}
