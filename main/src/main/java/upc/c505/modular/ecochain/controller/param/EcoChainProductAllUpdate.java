package upc.c505.modular.ecochain.controller.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EcoChainProductAllUpdate {
    @ApiModelProperty("联络人")
    private String contactName;

    @ApiModelProperty("联络人电话")
    private String contactPhone;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    @TableField("latitude")
    private String latitude;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("标签1")
    private String tagOne;

    @ApiModelProperty("标签2")
    private String tagTwo;

    @ApiModelProperty("社会信用代码")
    private String socialCreditCode;
}
