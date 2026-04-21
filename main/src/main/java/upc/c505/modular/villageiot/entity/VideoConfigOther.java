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
@TableName("video_config_other")
@ApiModel(value = "VideoConfigOther对象", description = "")
public class VideoConfigOther implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("其他视频配置表的id")
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

    @ApiModelProperty("其它设备类型，目前有HLS和RTMP两种")
    @TableField("device_type")
    private String deviceType;

    @ApiModelProperty("监控点名称")
    @TableField("point_name")
    private String pointName;

    @ApiModelProperty("视频流地址")
    @TableField("stream_address")
    private String streamAddress;

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
