package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import upc.c505.modular.ecochain.entity.EcoChainProductManager;

/**
 * @Author: xth
 * @Date: 2024/10/18 16:04
 */
@Getter
@Setter
@Accessors(chain = true)
public class EcoChainInsertAndUpdateProductParam extends EcoChainProductManager {

    @ApiModelProperty("地址前缀")
    private String addressPrefix;

}
