package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainCollectProductParam {
    @ApiModelProperty("产品图片")
    private String productPicture;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("产品参数id")
    private String productParameterId;

    @ApiModelProperty("是否被删除，默认为0")
    private Integer isDelete;

    @ApiModelProperty("收藏表id")
    private Long collectId;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("收藏人id")
    private Long collectPeopleId;

    @ApiModelProperty("住所/经营场所")
    private String businessPlace;

    @ApiModelProperty("市场主体名称")
    private String supEnterpriseName;
}
