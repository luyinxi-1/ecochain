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
 * @author byh
 * @since 2024-09-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_product_manager")
@ApiModel(value = "EcoChainProductManager对象", description = "")
public class EcoChainProductManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品管理表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("产品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("状态（0停用，1启用）")
    @TableField("status")
    private String status;

    @ApiModelProperty("置顶（0不置顶，1置顶）")
    @TableField("top_up")
    private String topUp;

    @ApiModelProperty("详细介绍（富文本）")
    @TableField("detailed_introduction")
    private String detailedIntroduction;

    @ApiModelProperty("图片")
    @TableField("picture")
    private String picture;

    @ApiModelProperty("产品标签id")
    @TableField("eco_chain_product_tag_id")
    private Long ecoChainProductTagId;

    @ApiModelProperty("产品参数id（多个拼接）")
    @TableField("eco_chain_product_parameter_id")
    private String ecoChainProductParameterId;

    @ApiModelProperty("产品分类id（多个拼接）")
    @TableField("eco_chain_product_classification_id")
    private String ecoChainProductClassificationId;

    @ApiModelProperty("跳转平台")
    @TableField("redirect_platform")
    private String redirectPlatform;

    @ApiModelProperty("跳转平台地址")
    @TableField("redirect_platform_url")
    private String redirectPlatformUrl;

    @ApiModelProperty("是否可以跳转（0不能，1可以）")
    @TableField("can_redirect")
    private Long canRedirect;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("配置企业名称")
    @TableField("configured_enterprise_name")
    private String configuredEnterpriseName;

    @ApiModelProperty("配置企业社会信用代码")
    @TableField("configured_social_credit_code")
    private String configuredSocialCreditCode;

    @ApiModelProperty("联络人")
    @TableField("contact_name")
    private String contactName;

    @ApiModelProperty("联络人电话")
    @TableField("contact_phone")
    private String contactPhone;

    @ApiModelProperty("区域id")
    @TableField("area_id")
    private Long areaId;

    @ApiModelProperty("经度")
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private String latitude;

    @ApiModelProperty("地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("标签1")
    @TableField("tag_one")
    private String tagOne;

    @ApiModelProperty("标签2")
    @TableField("tag_two")
    private String tagTwo;

    @ApiModelProperty("类型")
    @TableField("type")
    private String type;

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