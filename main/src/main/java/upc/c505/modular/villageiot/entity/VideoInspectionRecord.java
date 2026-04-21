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
 * @since 2024-06-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("video_inspection_record")
@ApiModel(value = "VideoInspectionRecord对象", description = "")
public class VideoInspectionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("巡查记录表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("监控属性")
    @TableField("monitor_attributes")
    private String monitorAttributes;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("许可证号")
    @TableField("coa_code")
    private String coaCode;

    @ApiModelProperty("信用代码")
    @TableField("ent_credit_code")
    private String entCreditCode;

    @ApiModelProperty("许可类型")
    @TableField("coa_type")
    private String coaType;

    @ApiModelProperty("经营地址")
    @TableField("business_address")
    private String businessAddress;

    @ApiModelProperty("所属区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("所属区域")
    @TableField("area_name")
    private String areaName;

    @ApiModelProperty("负责人")
    @TableField("principal")
    private String principal;

    @ApiModelProperty("联系电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("年度违规次数")
    @TableField("yearly_violation_frequency")
    private Integer yearlyViolationFrequency;

    @ApiModelProperty("巡查时间")
    @TableField("inspection_time")
    private LocalDateTime inspectionTime;

    @ApiModelProperty("取证时间")
    @TableField("confirm_time")
    private LocalDateTime confirmTime;

    @ApiModelProperty("巡查方式（0是人工，1是AI）")
    @TableField("inspection_way")
    private Integer inspectionWay;

    @ApiModelProperty("巡查照片")
    @TableField("picture")
    private String picture;

    @ApiModelProperty("巡查记录")
    @TableField("record")
    private String record;

    @ApiModelProperty("巡查处理人")
    @TableField("handle")
    private String handle;

    @ApiModelProperty("处理意见")
    @TableField("handling_opinions")
    private String handlingOpinions;

    @ApiModelProperty("处理时间")
    @TableField("handle_time")
    private LocalDateTime handleTime;

    @ApiModelProperty("是否答复（0否，1是）")
    @TableField("is_reply")
    private Integer isReply;

    @ApiModelProperty("答复人")
    @TableField("reply")
    private String reply;

    @ApiModelProperty("答复时间")
    @TableField("reply_time")
    private LocalDateTime replyTime;

    @ApiModelProperty("答复信息")
    @TableField("reply_info")
    private String replyInfo;

    @ApiModelProperty("答复的附件")
    @TableField("enclosure")
    private String enclosure;

    @ApiModelProperty("创建人")
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("添加时间")
    @TableField(value = "add_datetime", fill = FieldFill.INSERT)
    private LocalDateTime addDatetime;

    @ApiModelProperty("操作人")
    @TableField(value = "operator", fill = FieldFill.UPDATE)
    private String operator;

    @ApiModelProperty("修改时间")
    @TableField(value = "operation_datetime", fill = FieldFill.UPDATE)
    private LocalDateTime operationDatetime;


}
