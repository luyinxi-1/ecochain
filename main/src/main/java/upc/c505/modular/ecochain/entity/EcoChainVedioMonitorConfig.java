package upc.c505.modular.ecochain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author mjh
 * @since 2024-09-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_vedio_monitor_config")
@ApiModel(value = "EcoChainVedioMonitorConfig对象", description = "")
public class EcoChainVedioMonitorConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("生态链视频监控配置")
    @TableId("id")
    private Long id;

    @ApiModelProperty("video_config_common的id，用于取出公共字段")
    @TableField("video_config_common_id")
    private Integer videoConfigCommonId;

    @ApiModelProperty("监控摄像头id 关联视频配置表video_config_common")
    @TableField("monitor_id")
    private Integer monitorId;

    @ApiModelProperty("设备类型（海康、大华等）")
    @TableField("device_type")
    private String deviceType;

    @ApiModelProperty("监控点名称")
    @TableField("point_name")
    private String pointName;

    @ApiModelProperty("区域组id 关联eco_chain_regional_configuration表")
    @TableField("region_id")
    private Long regionId;

    @ApiModelProperty("是否对外公示（0不公示，1公示）")
    @TableField("is_public")
    private Integer isPublic;

    @ApiModelProperty("关联项目")
    @TableField("related_project")
    private String relatedProject;

    @ApiModelProperty("缩略图")
    @TableField("thumbnail_image")
    private String thumbnailImage;

    @ApiModelProperty("创建人")
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("创建时间")
    @TableField(value = "add_datetime", fill = FieldFill.INSERT)
    private LocalDateTime addDatetime;

    @ApiModelProperty("操作人")
    @TableField(value = "operator", fill = FieldFill.UPDATE)
    private String operator;

    @ApiModelProperty("操作时间")
    @TableField(value = "operation_datetime", fill = FieldFill.UPDATE)
    private LocalDateTime operationDatetime;


}
