package upc.c505.modular.filemanage.utils;

public class TargetIndexUtil {
    /**
     * 寻找出现第n次字符的索引
     * @param source 原始字符串
     * @param target 匹配的字符串
     * @param n 匹配的字符串第n个
     * @return int
     **/
    public static int indexOfTarget(String source, String target, int n) {
        int index = source.indexOf(target);
        int i = 0;
        while (index != -1) {
            index = source.indexOf(target, index + i);
            i++;
            if (i == n) {
                break;
            }
        }
        return index;
    }
}
