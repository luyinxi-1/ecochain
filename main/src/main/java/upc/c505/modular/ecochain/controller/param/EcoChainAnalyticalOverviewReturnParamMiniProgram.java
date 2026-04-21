package upc.c505.modular.ecochain.controller.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @Description: 分析概览模块小程序端返回参数
 * @Author: la
 * @CreateTime: 2024-10-22
 */

@Data
@Accessors(chain = true)
public class EcoChainAnalyticalOverviewReturnParamMiniProgram {

    @ApiModelProperty("累计访客数")
    private Long totalVisitorCount;

    @ApiModelProperty("生态链扫码访问")
    private Long ecoChainScanVisitNum;

    @ApiModelProperty("企业扫码访问")
    private Long enterpriseScanVisitNum;

    @ApiModelProperty("产品扫码")
    private Long productScanNum;

    @ApiModelProperty("分享")
    private Long shareNumberNum;

    @ApiModelProperty("品牌故事")
    private Long brandStoryNum;

    @ApiModelProperty("基本信息")
    private Long basicInfoNum;

    @ApiModelProperty("荣誉资质")
    private Long honorQualificationNum;

    @ApiModelProperty("视频监控")
    private Long videoMonitoringNum;

    @ApiModelProperty("首页")
    private Long homePageNum;

    @ApiModelProperty("产品中心")
    private Long productCenterNum;

    @ApiModelProperty("联系老板")
    private Long contactBossNum;

    @ApiModelProperty("商家承诺")
    private Long merchantCommitmentNum;

    @ApiModelProperty("名片分享浏览")
    private Long businessCardShareBrowseNum;

    @ApiModelProperty("产品分享浏览")
    private Long productShareBrowseNum;

    @ApiModelProperty("公司主页分享浏览")
    private Long companyHomepageShareBrowseNum;
}
