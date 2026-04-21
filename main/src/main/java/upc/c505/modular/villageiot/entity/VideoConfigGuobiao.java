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
@TableName("video_config_guobiao")
@ApiModel(value = "VideoConfigGuobiao对象", description = "")
public class VideoConfigGuobiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("国标视频配置表的id")
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

    @ApiModelProperty("设备国标编号(视频流点播接口)")
    @TableField("device_id")
    private String deviceId;

    @ApiModelProperty("(视频流点播接口所需参数)")
    @TableField("channel_id")
    private String channelId;

    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("生产厂商")
    @TableField("manufacturer")
    private String manufacturer;

    @ApiModelProperty("型号")
    @TableField("model")
    private String model;

    @ApiModelProperty("固件版本")
    @TableField("firmware")
    private String firmware;

    @ApiModelProperty("transport传输协议（UDP/TCP）")
    @TableField("transport")
    private String transport;

    @ApiModelProperty("数据流传输模式")
    @TableField("stream_mode")
    private String streamMode;

    @TableField("ip")
    private String ip;

    @TableField("port")
    private Integer port;

    @ApiModelProperty("wan地址")
    @TableField("host_address")
    private String hostAddress;

    @ApiModelProperty("是否在线，1为在线，0为离线")
    @TableField("online")
    private Integer online;

    @ApiModelProperty("注册时间")
    @TableField("register_time")
    private String registerTime;

    @ApiModelProperty("心跳时间")
    @TableField("keepalive_time")
    private String keepaliveTime;

    @ApiModelProperty("通道个数")
    @TableField("channel_count")
    private Integer channelCount;

    @ApiModelProperty("注册有效期")
    @TableField("expires")
    private Integer expires;

    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty("设备使用的媒体id, 默认为null")
    @TableField("media_server_id")
    private String mediaServerId;

    @ApiModelProperty("符集, 支持 UTF-8 与 GB2312")
    @TableField("charset")
    private String charset;

    @ApiModelProperty("目录订阅周期，0为不订阅")
    @TableField("subscribe_cycle_for_catalog")
    private Integer subscribeCycleForCatalog;

    @ApiModelProperty("移动设备位置订阅周期，0为不订阅")
    @TableField("subscribe_cycle_for_mobile_position")
    private Integer subscribeCycleForMobilePosition;

    @ApiModelProperty("移动设备位置信息上报时间间隔,单位:秒,默认值5")
    @TableField("mobile_position_submission_interval")
    private Integer mobilePositionSubmissionInterval;

    @ApiModelProperty("报警心跳时间订阅周期，0为不订阅")
    @TableField("subscribe_cycle_for_alarm")
    private Integer subscribeCycleForAlarm;

    @ApiModelProperty("是否开启ssrc校验,默认关闭，开启可以防止串流")
    @TableField("ssrc_check")
    private Integer ssrcCheck;

    @ApiModelProperty("地理坐标系， 目前支持 WGS84,GCJ02")
    @TableField("geo_coord_sys")
    private String geoCoordSys;

    @ApiModelProperty("树类型 国标规定了两种树的展现方式 行政区划：CivilCode 和业务分组:BusinessGroup")
    @TableField("tree_type")
    private String treeType;

    @TableField("password")
    private String password;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private String latitude;

    @ApiModelProperty("经度")
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty("收流IP")
    @TableField("sdp_ip")
    private String sdpIp;

    @ApiModelProperty("创建人")
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("创建时间")
    @TableField(value = "add_datetime", fill = FieldFill.INSERT)
    private LocalDateTime addDatetime;

    @ApiModelProperty("操作人")
    @TableField(value = "operator", fill = FieldFill.INSERT_UPDATE)
    private String operator;

    @ApiModelProperty("操作时间")
    @TableField(value = "operation_datetime", fill = FieldFill.UPDATE)
    private LocalDateTime operationDatetime;


}
