package upc.c505.modular.miniprogram.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import upc.c505.common.requestparam.PageBaseSearchParam;

/**
 * 小程序绑定查询参数
 */
@Data
@Accessors(chain = true)
public class MiniProgramBindSearchParam extends PageBaseSearchParam {
    /**
     * 搜索关键词（支持企业名称、appId、小程序名称、信用代码的模糊查询）
     */
    @ApiModelProperty("搜索关键词（支持企业名称、appId、小程序名称、信用代码的模糊查询）")
    private String keyword;
}
