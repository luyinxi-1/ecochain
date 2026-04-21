package upc.c505.modular.ecochain.controller.param;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ExcelIgnoreUnannotated
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
@Accessors(chain = true)
public class ExportEcoChainWorkStatisticsSignContractRateExcelParam {
    @ApiModelProperty("人员名字")
    @ExcelProperty("人员名字")
    private String name;

    @ApiModelProperty("跟踪项目数量")
    @ExcelProperty("跟踪项目数量")
    private Integer trackingNum;

    @ApiModelProperty("验收记录数量")
    @ExcelProperty("验收记录数量")
    private Integer completeRecordNum;

    @ApiModelProperty("签单率")
    @ExcelProperty("签单率")
    @NumberFormat(value = "0.00%")
    private Double rate;

    @ApiModelProperty("排名")
    @ExcelProperty("排名")
    private Integer ranking;
}
