package upc.c505.modular.dict.controller.searchParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IdParam {
    @ApiModelProperty(value = "ID列表,角色传入realId")
    List<Integer> idList = new ArrayList<>();
}
