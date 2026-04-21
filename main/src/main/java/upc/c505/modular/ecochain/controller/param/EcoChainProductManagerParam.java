package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.modular.ecochain.entity.EcoChainProductManager;
import upc.c505.modular.ecochain.entity.EcoChainProductManagerValueContent;

import java.util.List;

/**
 * @Author: xth
 * @Date: 2024/9/26 18:50
 */
@Data
@Accessors(chain = true)
public class EcoChainProductManagerParam extends EcoChainProductManager {
    @ApiModelProperty(value = "产品管理-值内容关联表列表")
    List<EcoChainProductManagerValueContent> productManagerValueContent;
}
