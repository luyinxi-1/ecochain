package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDateTime;

/**
 * @Description: 视频监控配置分页查询参数类
 * @Author: mjh
 * @CreateTime: 2024-09-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EcoChainVedioMonitorConfigPageSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("生态链视频监控配置")
    private Long id;

    @ApiModelProperty("video_config_common的id，用于取出公共字段")
    private Integer videoConfigCommonId;

    @ApiModelProperty("监控摄像头id 关联视频配置表video_config_common")
    private Integer monitorId;

    @ApiModelProperty("设备类型（海康、大华等）")
    private String deviceType;

    @ApiModelProperty("模糊搜索参数 监控点名称")
    private String pointName;

    @ApiModelProperty("区域组id 关联eco_chain_regional_configuration表")
    private Long regionId;

    @ApiModelProperty("是否对外公示（0不公示，1公示）")
    private Integer isPublic;

    @ApiModelProperty("关联项目")
    private String relatedProject;

    @ApiModelProperty("社会信用代码")
    String socialCreditCode;

    @ApiModelProperty("缩略图")
    private String thumbnailImage;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private LocalDateTime addDatetime;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationDatetime;
}
