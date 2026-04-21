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
@TableName("eco_chain_enterprise_publicity")
@ApiModel(value = "EcoChainEnterprisePublicity对象", description = "")
public class EcoChainEnterprisePublicity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("企业主体宣传表")
    @TableId("id")
    private Long id;

    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("企业名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("是否启用（0：未启用；1：启用）")
    @TableField("enable_or_not")
    private Integer enableOrNot;

    @ApiModelProperty("宣传口号")
    @TableField("propaganda_slogan")
    private String propagandaSlogan;

    @ApiModelProperty("上传日期")
    @TableField(value = "add_datetime", fill = FieldFill.INSERT)
    private LocalDateTime addDatetime;

    @ApiModelProperty("上传人")
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("图片")
    @TableField("photo")
    private String photo;


}
