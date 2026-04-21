package upc.c505.modular.ecochain.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
@Data
@Accessors(chain = true)
public class EcoChainIdParam {
    @ApiModelProperty(value = "ID列表,角色传入realId")
    List<Integer> idList = new ArrayList<>();
}
