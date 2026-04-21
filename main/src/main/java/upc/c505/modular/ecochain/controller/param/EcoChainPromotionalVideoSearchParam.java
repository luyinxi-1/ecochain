package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * @Author: xth
 * @Date: 2025/2/15 15:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EcoChainPromotionalVideoSearchParam extends PageBaseSearchParam {
    @ApiModelProperty("社会信用代码")
    @TableField("social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty("序号")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("视频名称")
    @TableField("video_name")
    private String videoName;
}
