package upc.c505.utils;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class StringUtil {
    public static List<Long> splitToListLong(String str) {
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        Pattern pattern = Pattern.compile("[0-9]*");
        List<String> stringList = strList.stream().filter(strItem ->pattern.matcher(strItem).matches()).collect(Collectors.toList());
        List<Long> longList = stringList.stream().map(strItem -> Long.parseLong(strItem)).collect(Collectors.toList());
        return longList;
    }
}
