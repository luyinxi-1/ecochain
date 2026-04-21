package upc.c505.modular.ecochain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @since 2024-09-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_extended_table")
@ApiModel(value = "EcoChainExtendedTable对象", description = "")
public class EcoChainExtendedTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主体表扩充表（减少对原主体表的修改）")
    @TableId("id")
    private Long id;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("商家承诺")
    @TableField("merchant_promises")
    private String merchantPromises;

    @ApiModelProperty("企业介绍")
    @TableField("company_introduction")
    private String companyIntroduction;

    @ApiModelProperty("联系电话1")
    @TableField("phone_number_one")
    private String phoneNumberOne;

    @ApiModelProperty("联系电话2")
    @TableField("phone_number_two")
    private String phoneNumberTwo;

    @ApiModelProperty("微信二维码")
    @TableField("wechat_QR_code")
    private String wechatQrCode;

    @ApiModelProperty("深色logo")
    @TableField("dark_logo")
    private String darkLogo;

    @ApiModelProperty("浅色logo")
    @TableField("light_color_logo")
    private String lightColorLogo;

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

    @ApiModelProperty("福利口号")
    @TableField("welfare_slogan")
    private String welfareSlogan;

    @ApiModelProperty("是否开启信息授权提醒（0:未开启;1:开启）")
    @TableField("message_authorization_notification")
    private Integer messageAuthorizationNotification;

    @ApiModelProperty("色彩版本")
    @TableField("color_version")
    private String colorVersion;

    @ApiModelProperty("vr展厅")
    @TableField("vr_exhibition")
    private String vrExhibition;

    @ApiModelProperty("小程序主页配置")
    @TableField("mini_program_homepage_configuration")
    private String miniProgramHomepageConfiguration;

    @ApiModelProperty("小程序分享展示配置")
    @TableField("mini_program_share_display_configuration")
    private String miniProgramShareDisplayConfiguration;

    @ApiModelProperty("小程序分享主页配置")
    @TableField("mini_program_share_homepage_configuration")
    private String miniProgramShareHomepageConfiguration;

    @ApiModelProperty("工作记录加分")
    @TableField("work_record_marks")
    private Long workRecordMarks;

    @ApiModelProperty("签单成交加分")
    @TableField("sign_contract_marks")
    private Long signContractMarks;

    @ApiModelProperty("企业分享的背景照片")
    @TableField("background_picture")
    private String backgroundPicture;

    @ApiModelProperty("产品列表样式")
    @TableField("product_list_style")
    private String productListStyle;

    @ApiModelProperty("产品详情样式")
    @TableField("product_detail_style")
    private String productDetailStyle;

    @ApiModelProperty("热度是否显示（0：不显示；1：显示）")
    @TableField("display_heat")
    private Integer displayHeat;

    @ApiModelProperty("品牌故事是否显示（0：不显示；1：显示）")
    @TableField("display_brand_story")
    private Integer displayBrandStory;

    @ApiModelProperty("基本信息")
    @TableField("basic_info")
    private String basicInfo;

    @ApiModelProperty("产品详情标题")
    @TableField("product_detail_title")
    private String productDetailTitle;

    @ApiModelProperty("产品提示")
    @TableField("product_instruction")
    private String productInstruction;

    @ApiModelProperty("产品标题")
    @TableField("product_title")
    private String productTitle;

    @ApiModelProperty("横幅")
    @TableField("banner")
    private String banner;

    @ApiModelProperty("对外分享是否显示标题（0:不显示;1:显示）")
    @TableField("share_display_title")
    private Integer shareDisplayTitle;
}
