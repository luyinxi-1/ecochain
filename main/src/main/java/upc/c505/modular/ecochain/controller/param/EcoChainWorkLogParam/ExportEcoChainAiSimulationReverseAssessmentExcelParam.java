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
public class ExportEcoChainAiSimulationReverseAssessmentExcelParam {

    @ApiModelProperty("人员姓名")
    @ExcelProperty("人员姓名")
    private String name;

    @ApiModelProperty("过程跟踪数量")
    @ExcelProperty("过程跟踪数量")
    private Integer processTrackingNumber;

    @ApiModelProperty("完成记录数量")
    @ExcelProperty("完成记录数量")
    private Integer completedRecordsNumber;

    @ApiModelProperty("工作得分")
    @ExcelProperty("工作得分")
    private Integer workScore;

    @ApiModelProperty("绩效得分")
    @ExcelProperty("绩效得分")
    private Integer performanceScore;

    @ApiModelProperty("基础分")
    @ExcelProperty("基础分")
    private Integer basicScore;

    @ApiModelProperty("总计得分")
    @ExcelProperty("总计得分")
    private Integer totalScore;

    @ApiModelProperty("k值")
    @ExcelProperty("k值")
    private Double kValue;

    @ApiModelProperty("排名")
    @ExcelProperty("排名")
    private Integer rank;
}
