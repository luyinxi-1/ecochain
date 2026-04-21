package upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.maintain.controller.converter.LocalDateTimeConverter;

import java.time.LocalDateTime;

/**
 * @Author: xth
 * @Date: 2025/4/16 21:11
 */
@Data
@ExcelIgnoreUnannotated
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
@Accessors(chain = true)
public class ExportWorkLogExcelParam {
    @ApiModelProperty("详细类型选择（种植品种、装饰类型、养殖品种、工作类型、维修类型、卤制种类）")
    @ExcelProperty("类型")
    private String detailTypeOption;

    @ApiModelProperty("产业分组选择（地块名称、项目组、养殖区块、房间号、维修车间、加工区域）")
    @ExcelProperty("区域")
    private String industryGroupOption;

    @ApiModelProperty("项目名称")
    @ExcelProperty("项目名称")
    private String industryWarehouse;

    @ApiModelProperty("企业名称")
    @ExcelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("状态(0未开始，1种植中，2采摘中，3完成，-1查询0/1/2三种状态)")
    @ExcelProperty("状态")
    private String status;

    @ApiModelProperty("记录时间")
    @ExcelProperty(value = "记录时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime recordingTime;

    @ApiModelProperty("记录人")
    @ExcelProperty("记录人")
    private String recorder;

    @ApiModelProperty("过程节点名称")
    @ExcelProperty("过程节点")
    private String ecoChainProcessNodeConfigurationName;

    @ApiModelProperty("过程名称")
    @ExcelProperty("过程名称")
    private String processName;

    @ApiModelProperty("描述")
    @ExcelProperty("描述")
    private String recordDescribe;

    @ApiModelProperty("位置")
    @ExcelProperty("位置")
    private String location;

    @ApiModelProperty("评价人")
    @ExcelProperty("评价人")
    private String evaluator;

    @ApiModelProperty("评价时间")
    @ExcelProperty(value = "评价时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime evaluationDate;

    @ApiModelProperty("评价结果")
    @ExcelProperty("评价结果")
    private String evaluateResults;

    @ApiModelProperty("照片")
    @ExcelProperty(value = "照片", index = 14)
    private byte[] picture;
}
