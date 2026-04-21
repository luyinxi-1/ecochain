package upc.c505.modular.villageiot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author frd
 * @since 2024-03-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("video_config_esurfing")
@ApiModel(value = "VideoConfigEsurfing对象", description = "")
public class VideoConfigEsurfing implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("天翼视频配置表的id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("video_config_common的id，用于取出公共字段")
    @TableField("video_config_common_id")
    private Integer videoConfigCommonId;

    @ApiModelProperty("所属区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("所属区域")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty("缩略图")
    @TableField("picture")
    private String picture;

    @ApiModelProperty("设备码")
    @TableField("device_code")
    private String deviceCode;

    @ApiModelProperty("监控点名称")
    @TableField("device_name")
    private String deviceName;

    @ApiModelProperty("设备类型号码")
    @TableField("device_model")
    private String deviceModel;

    @ApiModelProperty("厂商Id")
    @TableField("factory_id")
    private Integer factoryId;

    @ApiModelProperty("设备类型")
    @TableField("device_factory")
    private String deviceFactory;

    @ApiModelProperty("设备类型1：云眼 设备3：拉取看家 设备4: 云眼NVR 5：绑定看家 设备6：拉取国标 设备7：绑定国标 设备8：级联国标 设备9:小翼管家 设备A10:小翼管家 设备11:社标")
    @TableField("device_type")
    private Integer deviceType;

    @ApiModelProperty("设备固件版本")
    @TableField("firmware_version")
    private String firmwareVersion;

    @ApiModelProperty("是否为云化摄像头1：是 0：否")
    @TableField("is_cloud_camera")
    private Integer isCloudCamera;

    @ApiModelProperty("国标Id")
    @TableField("gb_id")
    private String gbId;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private String latitude;

    @ApiModelProperty("经度")
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty("在线状态 1:在线 0:离线")
    @TableField("online_status")
    private Integer onlineStatus;

    @ApiModelProperty("云存状态 1:有效 0:无效")
    @TableField("cloud_status")
    private Integer cloudStatus;

    @ApiModelProperty("绑定状态 1:已绑定 0:待绑定")
    @TableField("band_status")
    private Integer bandStatus;

    @ApiModelProperty("最近的绑定时间,格式：yyyy-MM-dd HH:mm:ss")
    @TableField("import_time")
    private LocalDateTime importTime;

    @ApiModelProperty("全链接区域名称")
    @TableField("full_region_name")
    private String fullRegionName;

    @ApiModelProperty("设备安装区域编码")
    @TableField("region_code")
    private String regionCode;

    @ApiModelProperty("云存套餐信息")
    @TableField("package_info")
    private String packageInfo;

    @ApiModelProperty("设备CTEI码")
    @TableField("ctei")
    private String ctei;

    @ApiModelProperty("是否自己绑定的设备1：是 0：否")
    @TableField("is_self_bind")
    private Integer isSelfBind;

    @ApiModelProperty("直播地址类型 (1.rtsp; 2.rtmp; 3.hls)默认3")
    @TableField("proto")
    private Integer proto;

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
