package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import upc.c505.modular.ecochain.entity.EcoChainPromotionalVideo;

/**
 * @Author: xth
 * @Date: 2025/5/26 10:36
 */
@Getter
@Setter
@Accessors(chain = true)
public class EcoChainInsertAndUpdatePromotionalVideo extends EcoChainPromotionalVideo {

    @ApiModelProperty("地址前缀")
    private String addressPrefix;
}
