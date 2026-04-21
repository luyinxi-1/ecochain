package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDate;

/**
 * @Author: xth
 * @Date: 2024/9/23 10:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EcoChainTypeConfigurationPageSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("详细类型（种植品种、装饰类型、养殖品种、工作类型、维修类型、卤制种类）")
    @TableField("detail_type")
    private String detailType;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;
}
