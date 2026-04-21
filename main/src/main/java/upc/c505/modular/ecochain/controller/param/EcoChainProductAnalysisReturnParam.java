package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @Author: xth
 * @Date: 2024/10/16 15:39
 */
@Data
@Accessors(chain = true)
public class EcoChainProductAnalysisReturnParam {
    @ApiModelProperty("每小时累计访客/分享数")
    private List<EcoChainProductAnalyticalChartNameValueParam> hourlyVisitorCounts;

    @ApiModelProperty("每天累计访客/分享数")
    private List<EcoChainProductAnalyticalChartNameValueParam> dailyVisitorCounts;

//    @ApiModelProperty("每小时累计访客数")
//    private Map<Integer, Long> hourlyVisitorCounts;
//
//    @ApiModelProperty("每天累计访客数")
//    private Map<LocalDate, Long> dailyVisitorCounts;
//
//    @ApiModelProperty("每小时累计分享数")
//    private Map<Integer, Long> hourlyShareCounts;
//
//    @ApiModelProperty("每天累计分享数")
//    private Map<LocalDate, Long> dailyShareCounts;
}
