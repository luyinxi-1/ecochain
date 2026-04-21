package upc.c505.modular.villageiot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author frd
 * @since 2024-01-19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("video_config_common")
@ApiModel(value = "VideoConfigCommon对象", description = "")
public class VideoConfigCommon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("视频配置表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("监控属性（重点单位、重点项目、党组织会议室、产业）")
    @TableField("monitor_attributes")
    private String monitorAttributes;

    @ApiModelProperty("所属区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("所属区域")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty("监控组名称（企业名称）")
    @TableField("monitor_group_name")
    private String monitorGroupName;

    @ApiModelProperty("社会信用代码/项目编号/党组织id")
    @TableField("ent_credit_code")
    private String entCreditCode;

    @ApiModelProperty("设备类型（海康、大华等）")
    @TableField("device_type")
    private String deviceType;

    @ApiModelProperty("当前设备数量")
    @TableField("current_device_number")
    private Integer currentDeviceNumber;

    @ApiModelProperty("许可类别（许可证类型）")
    @TableField("coa_type")
    private String coaType;

    @ApiModelProperty("许可证号")
    @TableField("coa_code")
    private String coaCode;

    @ApiModelProperty("播放器类型")
    @TableField("video_sign")
    private String videoSign;

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

    @ApiModelProperty("企业在线视频数量")
    @TableField(exist = false)
    private Long onlineNumber;
}
