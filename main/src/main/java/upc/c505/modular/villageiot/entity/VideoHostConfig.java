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
 * @since 2023-11-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("video_host_config")
@ApiModel(value = "VideoHostConfig对象", description = "")
public class VideoHostConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("host配置表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("所属区域")
    @TableField("area")
    private String area;

    @ApiModelProperty("所属区域id")
    @TableField("area_id")
    private Integer areaId;

    @ApiModelProperty("设备平台类型")
    @TableField("device_type")
    private String deviceType;

    @ApiModelProperty("取流协议")
    @TableField("protocol")
    private String protocol;

    @ApiModelProperty("host地址")
    @TableField("host_address")
    private String hostAddress;

    @ApiModelProperty("appkey")
    @TableField("appkey")
    private String appkey;

    @ApiModelProperty("appsecret")
    @TableField("appsecret")
    private String appSecret;

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

    @ApiModelProperty("宇视(uniview):存放两个密钥,RSA公钥字段E和N字段，以下划线\"_\"分隔")
    @TableField("other_password")
    private String otherPassword;


}
