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
 * @since 2024-09-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_main_promotional_image")
@ApiModel(value = "EcoChainMainPromotionalImage对象", description = "")
public class EcoChainMainPromotionalImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("生态链宣传主图表")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("照片名称")
    @TableField("picture_name")
    private String pictureName;

    @ApiModelProperty("轮播顺序")
    @TableField("rotation_sequence")
    private Integer rotationSequence;

    @ApiModelProperty("状态（0表示停用，1表示启用）")
    @TableField("status")
    private String status;

    @ApiModelProperty("宣传口号")
    @TableField("promotion_slogan")
    private String promotionSlogan;

    @ApiModelProperty("图片")
    @TableField("picture")
    private String picture;

    @ApiModelProperty("企业名称")
    @TableField("enterprise_name")
    private String enterpriseName;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("区域id")
    @TableField("area_id")
    private Long areaId;

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
