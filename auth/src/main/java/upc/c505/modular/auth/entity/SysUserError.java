package upc.c505.modular.auth.entity;

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
 * @author sxz
 * @since 2023-07-30
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_error")
@ApiModel(value = "SysUserError对象", description = "")
public class SysUserError implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    @TableField("user_code")
    private String userCode;

    @ApiModelProperty("错误次数")
    @TableField("error")
    private Integer error;

    @ApiModelProperty("错误时间")
    @TableField("error_time")
    private LocalDateTime errorTime;

    @ApiModelProperty("创建时间")
    @TableField(value = "add_datetime")
    private LocalDateTime addDatetime;

    @ApiModelProperty("操作时间")
    @TableField(value = "operation_datetime")
    private LocalDateTime operationDatetime;


}
