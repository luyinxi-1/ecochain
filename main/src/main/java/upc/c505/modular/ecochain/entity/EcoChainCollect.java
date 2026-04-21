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
 * @since 2025-01-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("eco_chain_collect")
@ApiModel(value = "EcoChainCollect对象", description = "")
public class EcoChainCollect implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("收藏表")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("类型（0：企业  1：产品   2：名片）")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("收藏id（企业保存信用代码，名片保存企业人员id，产品保存产品id）")
    @TableField("collect_id")
    private String collectId;

    @ApiModelProperty("收藏人id（sys_user表id，新增时根据token获取 ）")
    @TableField("collect_people_id")
    private Long collectPeopleId;

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

    @ApiModelProperty("名称")
    @TableField("collect_name")
    private String collectName;

    @ApiModelProperty("是否删除(0未删除，1已删除)")
    private Integer isDelete;
}
