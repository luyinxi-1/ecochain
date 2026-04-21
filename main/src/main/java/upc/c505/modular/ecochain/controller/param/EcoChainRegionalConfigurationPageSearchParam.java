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
 * @Date: 2024/9/23 16:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EcoChainRegionalConfigurationPageSearchParam extends PageBaseSearchParam {

    @ApiModelProperty("产业分组（地块名称、项目组、养殖区块、房间号、维修车间、加工区域）")
    @TableField("industry_group")
    private String industryGroup;

    @ApiModelProperty("开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;
}
