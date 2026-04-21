package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainCollectInsertParam {

    @ApiModelProperty("收藏表")
    private Long id;

    @ApiModelProperty("类型（0：企业  1：产品   2：名片）")
    private Integer type;

    @ApiModelProperty("收藏id（企业保存信用代码，名片保存企业人员id，产品保存产品id）")
    private String collectId;

    @ApiModelProperty("名称")
    private String collectName;
}
