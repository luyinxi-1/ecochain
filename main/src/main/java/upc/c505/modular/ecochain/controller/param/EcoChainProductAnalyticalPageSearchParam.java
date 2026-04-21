package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: xth
 * @Date: 2024/9/28 16:07
 */
@Data
@Accessors(chain = true)
public class EcoChainProductAnalyticalPageSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
