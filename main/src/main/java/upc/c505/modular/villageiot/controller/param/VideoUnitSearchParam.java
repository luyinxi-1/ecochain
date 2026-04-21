package upc.c505.modular.villageiot.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @author: frd
 * @create-date: 2024/3/21 16:56
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class VideoUnitSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("监控属性")
    private String monitorAttributes;

    @ApiModelProperty("模糊搜索传入参数")
    private String searchParam;

    @ApiModelProperty("项目类型（监控属性为重点项目时生效）")
    private String projectType;
}
