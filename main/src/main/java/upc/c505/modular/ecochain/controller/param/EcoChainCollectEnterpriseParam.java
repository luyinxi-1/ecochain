package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainCollectEnterpriseParam {

    @ApiModelProperty("市场主体名称")
    private String supEnterpriseName;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("住所/经营场所")
    private String businessPlace;

    @ApiModelProperty("经营范围")
    private String businessScope;

    @ApiModelProperty("深色logo")
    private String darkLogo;

    @ApiModelProperty("浅色logo")
    private String lightColorLogo;

    @ApiModelProperty("收藏表id")
    private Long collectId;

    @ApiModelProperty("社会信用代码")
    private String socialCreditCode;

}
