package upc.c505.modular.ecochain.controller.param;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 名称和值对
 * @Author: la
 * @CreateTime: 2024-09-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EcoChainDataStatisticsNameValuePairParam {
    private String name;
    private Long value;
}
