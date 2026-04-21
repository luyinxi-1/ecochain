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
 * @since 2025-02-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_promotional_video")
@ApiModel(value = "EcoChainPromotionalVideo对象", description = "")
public class EcoChainPromotionalVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("宣传视频")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("序号")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("介绍（富文本）")
    @TableField("introduction")
    private String introduction;

    @ApiModelProperty("视频地址")
    @TableField("video_address")
    private String videoAddress;

    @ApiModelProperty("视频名称")
    @TableField("video_name")
    private String videoName;

    @ApiModelProperty("视频封面")
    @TableField("video_cover")
    private String videoCover;

    @ApiModelProperty("是否是公众号文章（0：否，1：是）")
    @TableField("is_public_account_article")
    private Integer isPublicAccountArticle;

    @ApiModelProperty("公众号文章地址")
    @TableField("public_account_article_url")
    private String publicAccountArticleUrl;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("是否发布（0：否，1：是）")
    @TableField("is_publish")
    private String isPublish;

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
