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
@TableName("eco_chain_brand_story")
@ApiModel(value = "EcoChainBrandStory对象", description = "")
public class EcoChainBrandStory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("生态链品牌故事表")
    @TableId("id")
    private Long id;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("对应part1-part10")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("同一信用代码下的同一个type进行排序，一个type下可能有多条")
    @TableField("type_sorting_number")
    private Integer typeSortingNumber;

    @ApiModelProperty("每个part中的图片")
    @TableField("photo")
    private String photo;

    @ApiModelProperty("图片描述")
    @TableField("picture_description")
    private String pictureDescription;

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
