package upc.c505.modular.villageiot.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: frd
 * @create-date: 2024/6/24 10:02
 */

@Data
@Accessors(chain = true)
public class VideoChannelUrlParam {
    @ApiModelProperty("RSA公钥字段E字段")
    @JsonProperty("RSAPublicKeyE")
    private String RSAPublicKeyE;

    @ApiModelProperty("RSA公钥字段N字段")
    @JsonProperty("RSAPublicKeyN")
    private String RSAPublicKeyN;
}
