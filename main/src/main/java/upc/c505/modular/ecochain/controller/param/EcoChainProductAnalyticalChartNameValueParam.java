package upc.c505.modular.ecochain.controller.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: xth
 * @Date: 2024/10/22 16:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EcoChainProductAnalyticalChartNameValueParam {
    private String name;
    private Long visitorCountsValue;
    private Long shareCountsValue;
    private Long productShareCountsValue;
}
