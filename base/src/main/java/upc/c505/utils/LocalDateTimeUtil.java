package upc.c505.utils;

import upc.c505.constant.DateFormatConst;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author: frd
 * @create-date: 2024/4/29 16:54
 */

public class LocalDateTimeUtil {
    /**
     * 根据字符串格式化为LocalDateTime类型,格式为yyyy-MM-dd HH:mm:ss
     */
    public static LocalDateTime formatForLocalDateTime(String s) {
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(DateFormatConst.DATE_TIME_COMMON_FORMAT_PATTERN));
    }

    public static LocalDate formatForLocalDate(String s) {
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(DateFormatConst.DATE_TIME_COMMON_FORMAT_PATTERN))
                .toLocalDate();
    }

    public static LocalTime formatForLocalTime(String s) {
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(DateFormatConst.DATE_TIME_COMMON_FORMAT_PATTERN))
                .toLocalTime();
    }

    /**
     *字符串转UTC格式时间
     */
    public static String transToUTCDate(String strDate) throws ParseException {
        Date date = new SimpleDateFormat(DateFormatConst.DATE_TIME_COMMON_FORMAT_PATTERN).parse(strDate);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormatConst.DATE_UTC_COMMON_FORMAT_PATTERN);

        //format将Date型转换为String型，parse将String型转换为Date型
        String UTCDate = sdf.format(date);
        return UTCDate;
    }

    /**
     * UTC时间格式转LocalDateTime
     */
    public static LocalDateTime utcToLocalDateTime(String utcTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmXXX");
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(utcTime, formatter);
        return offsetDateTime.toLocalDateTime();
    }

    private LocalDateTimeUtil() {

    }
}