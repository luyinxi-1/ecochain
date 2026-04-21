package upc.c505.modular.ecochain.controller.param.excelparam;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 生态链建库导入参数
 * @Author: xth
 * @Date: 2026/3/16
 */
@Data
@ExcelIgnoreUnannotated
public class EcoChainBuildWarehouseImportParam {

    @ApiModelProperty("类型")
    @ExcelProperty(value = "类型", index = 0)
    private String detailType;

    @ApiModelProperty("所属所")
    @ExcelProperty(value = "所属所", index = 1)
    private String industryGroup;

    @ApiModelProperty("项目名称")
    @ExcelProperty(value = "项目名称", index = 2)
    private String industryWarehouse;

    @ApiModelProperty("联系人")
    @ExcelProperty(value = "联系人", index = 3)
    private String warahouseContact;

    @ApiModelProperty("联系电话")
    @ExcelProperty(value = "联系电话", index = 4)
    private String warahousePhone;

    @ApiModelProperty("经营地址")
    @ExcelProperty(value = "经营地址", index = 5)
    private String notes;

    @ApiModelProperty("门头招牌名称")
    @ExcelProperty(value = "门头招牌名称", index = 6)
    private String shopSignName;

    @ApiModelProperty("监管所联系人")
    @ExcelProperty(value = "监管所联系人", index = 7)
    private String supervisorContact;

    @ApiModelProperty("监管所联系电话")
    @ExcelProperty(value = "监管所联系电话", index = 8)
    private String supervisorPhone;

    @ApiModelProperty("可见人")
    @ExcelProperty(value = "可见人", index = 9)
    private String visiblePeopleName;
}
