package upc.c505.utils;

import java.time.LocalDateTime;

/**
 * 用于把String型的年转换成LocalDateTime
 *
 * @author chenchen, wzy
 */
public class DateUtils {
    /**
     * 用于把String型的年转换成LocalDateTime
     */
    public static LocalDateTime generateByYear(String year) {
        return LocalDateTime.of(Integer.parseInt(year), 1, 1, 0, 0);
    }

    /**
     * 用于获取昨天0时0分的时间
     *
     * @return 昨天0时0分的时间
     */
    public static LocalDateTime getYesterday() {
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
    }

    /**
     * 用于获取今天0时0分的时间
     *
     * @return 今天0时0分的时间
     */
    public static LocalDateTime getTodayStart() {
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
    }

    public static LocalDateTime getTodayEnd() {
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59);
    }

    /**
     * 输入年月，返回某年某月LocalDateTime
     */
    public static LocalDateTime generateByYearAndMonth(Integer year, Integer month) {
        return LocalDateTime.of(year, month, 1, 0, 0);
    }

    /**
     * 返回现在的年份
     *
     * @return Integer型的年份
     */
    public static Integer getPresentYear() {
        return LocalDateTime.now().getYear();
    }

    private DateUtils() {

    }
}
