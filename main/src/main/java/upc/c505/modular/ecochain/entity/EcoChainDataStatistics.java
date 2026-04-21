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
 * @author la
 * @since 2024-09-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_data_statistics")
@ApiModel(value = "EcoChainDataStatistics对象", description = "")
public class EcoChainDataStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("生态链分析概览")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("统计类型（1：生态链扫码访问2：企业扫码访问3：产品扫码4：分享5：品牌故事6：基本信息7：荣誉资质8：视频监控9：首页10：产品中心11：联系老板12：商家承诺）")
    @TableField("statistical_type")
    private String statisticalType;

    @ApiModelProperty("访问时间")
    @TableField(value = "add_datetime", fill = FieldFill.INSERT)
    private LocalDateTime addDatetime;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("微信头像")
    @TableField("wechat_avatar")
    private String wechatAvatar;

    @ApiModelProperty("微信名")
    @TableField("wechat_name")
    private String wechatName;

    @ApiModelProperty("联系电话")
    @TableField("phone_number")
    private String phoneNumber;

    @ApiModelProperty("产品类型id（统计类型非产品扫码或者分享时此字段为空）")
    @TableField("eco_chain_product_classification_id")
    private String ecoChainProductClassificationId;

    @ApiModelProperty("产品类型（统计类型非产品扫码或者分享时此字段为空）")
    @TableField("product_classification")
    private String productClassification;

    @ApiModelProperty("产品id（统计类型非产品扫码或者分享时此字段为空）")
    @TableField("eco_chain_product_manager_id")
    private Long ecoChainProductManagerId;

    @ApiModelProperty("产品名称（统计类型非产品扫码或者分享时此字段为空）")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("区域id")
    @TableField(value = "area_id")
    private Long areaId;

    @ApiModelProperty("创建人")
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("操作人")
    @TableField(value = "operator", fill = FieldFill.UPDATE)
    private String operator;

    @ApiModelProperty("操作时间")
    @TableField("operation_time")
    private LocalDateTime operationTime;


}
