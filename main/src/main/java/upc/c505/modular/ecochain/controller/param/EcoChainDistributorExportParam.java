package upc.c505.modular.ecochain.controller.param;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.maintain.controller.converter.LocalDateTimeConverter;

import java.time.LocalDateTime;
@Data
@ExcelIgnoreUnannotated
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
@Accessors(chain = true)
public class EcoChainDistributorExportParam {

    @ApiModelProperty("分享人信用代码")
    @ExcelProperty("分享人信用代码")
    private String sharerSocialCreditCode;

    @ApiModelProperty("分享人电话")
    @ExcelProperty("分享人电话")
    private String sharerPhone;

    @ApiModelProperty("分享人姓名")
    @ExcelProperty("分享人姓名")
    private String sharerName;

    @ApiModelProperty("注册人电话")
    @ExcelProperty("注册人电话")
    private String registrantPhone;

    @ApiModelProperty("注册人姓名")
    @ExcelProperty("注册人姓名")
    private String registrantName;

    @ApiModelProperty("注册人信用代码")
    @ExcelProperty("注册人信用代码")
    private String registrantSocialCreditCode;

    @ApiModelProperty("授权状态")
    @ExcelProperty("授权状态")
    private String authorizeStatus;

    @ApiModelProperty("授权到期日期")
    @ExcelProperty(value = "授权到期日期", converter = LocalDateTimeConverter.class)
    private LocalDateTime endDate;

    @ApiModelProperty("用户状态")
    @ExcelProperty("用户状态")
    private String userStatus;

    @ApiModelProperty("缴费日期")
    @ExcelProperty(value = "最近一次缴费日期", converter = LocalDateTimeConverter.class)
    private LocalDateTime paymentDate;

}
