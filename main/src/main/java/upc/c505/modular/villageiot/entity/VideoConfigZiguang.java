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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@TableName("video_config_ziguang")
@ApiModel(value = "VideoConfigZiguang对象", description = "")
public class VideoConfigZiguang implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("紫光视频配置表的id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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

    @ApiModelProperty("监控点名称")
    @TableField("point_name")
    private String pointName;

    @ApiModelProperty("通道号")
    @TableField("camera_id")
    private Integer cameraId;

    @ApiModelProperty("通道名称")
    @TableField("camera_name")
    private String cameraName;

    @ApiModelProperty("流类型: 0-主码流, 1-辅码流")
    @TableField("stream_type")
    private Integer streamType;

    @ApiModelProperty("流协议类型: 1-RTSP, 2-RTMP, 3-HLS	4.-HTTP_SSL,5-HTTP FLV,6-HTTPS FLV,7-WS FLV,8-WSS FLV")
    @TableField("stream_mode")
    private String streamMode;

    @ApiModelProperty("请求时间：url有效时长(单位:秒), 不带/传0, 默认10分钟有效")
    @TableField("keep_alive")
    private String keepAlive;

    @ApiModelProperty("访问网域类型: 0-局域网, 1-公网")
    @TableField("is_local")
    private String isLocal;

    @ApiModelProperty("设备编号")
    @TableField("device_number")
    private Integer deviceNumber;

    @ApiModelProperty("1在线，0不在线（智能扫描时更新）")
    @TableField("online")
    private Integer online;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private String latitude;

    @ApiModelProperty("经度")
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty("添加时间")
    @TableField(value = "add_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime addTime;

    @ApiModelProperty("操作人")
    @TableField(value = "operator", fill = FieldFill.INSERT_UPDATE)
    private String operator;

    @ApiModelProperty("修改时间")
    @TableField(value = "operate_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime operateTime;

    @ApiModelProperty("操作IP")
    @TableField(value = "operate_ip", fill = FieldFill.INSERT_UPDATE)
    private String operateIp;


}
