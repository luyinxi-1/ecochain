package upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
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
public class ExportEcoChainWorkStatisticsExcelParam {
    @ApiModelProperty("名称")
    @ExcelProperty("名称")
    private String name;

    @ApiModelProperty("过程跟踪数量")
    @ExcelProperty("过程跟踪数量")
    private Integer processTrackingNum;

    @ApiModelProperty("完成记录数量")
    @ExcelProperty("完成记录数量")
    private Integer completeRecordNum;

    @ApiModelProperty("总数")
    @ExcelProperty("总数")
    private Integer sumNum;

    @ApiModelProperty("排名")
    @ExcelProperty("排名")
    private Integer ranking;
}
