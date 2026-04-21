package upc.c505.modular.ecochain.entity;

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
 * @author xth
 * @since 2024-09-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_process_tracking")
@ApiModel(value = "EcoChainProcessTracking对象", description = "")
public class EcoChainProcessTracking implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("生态链过程追踪关联表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("种植库id")
    @TableField("eco_chain_build_warehouse_id")
    private Long ecoChainBuildWarehouseId;

    @ApiModelProperty("位置")
    @TableField("location")
    private String location;

    @ApiModelProperty("过程节点id（采摘：灌溉，施肥）")
    @TableField("eco_chain_process_node_configuration_id")
    private Long ecoChainProcessNodeConfigurationId;

    @ApiModelProperty("过程节点名称（采摘：灌溉，施肥）")
    @TableField("eco_chain_process_node_configuration_name")
    private String ecoChainProcessNodeConfigurationName;

    @ApiModelProperty("过程名称")
    @TableField("process_name")
    private String processName;

    @ApiModelProperty("点位坐标（经纬度）")
    @TableField("point_coordinates")
    private String pointCoordinates;

    @ApiModelProperty("描述")
    @TableField("record_describe")
    private String recordDescribe;

    @ApiModelProperty("过程图片")
    @TableField("process_pictures")
    private String processPictures;

    @ApiModelProperty("记录人")
    @TableField("recorder")
    private String recorder;

    @ApiModelProperty("记录时间")
    @TableField("recording_time")
    private LocalDateTime recordingTime;

    @ApiModelProperty("操作人")
    @TableField(value = "operator", fill = FieldFill.UPDATE)
    private String operator;

    @ApiModelProperty("操作时间")
    @TableField(value = "operation_datetime", fill = FieldFill.UPDATE)
    private LocalDateTime operationDatetime;


}
