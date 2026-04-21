package upc.c505.utils;

import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;

import java.util.Objects;

/**
 * 用于计算比率,保留两位小数
 *
 * @author wzy
 */
public class RateUtils {
    private RateUtils() {
    }

    /**
     * 计算比率，并保留两位小数
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return 保留两位小数的Double
     */
    public static Double getRate(Integer numerator, Integer denominator) {
        if (Objects.equals(denominator, 0)) {
            throw new BusinessException(BusinessErrorEnum.UNKNOWN_ERROR, "分母不能为0");
        }
        if (Objects.equals(numerator, 0)) {
            return 0.0;
        }
        return Double.valueOf(String.format("%.2f", numerator / (denominator + 0.0)));
    }
}
