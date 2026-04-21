package upc.c505.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author xcx
 */

public class GetPageInfoUtil {
    /**
     * 假分页，自己手动传值
     * @param current 前端所传的当前页(从1开始)
     * @param size    前端所传的当前页最大数量
     * @param list    所得数据以列表形式存放
     * @return 返回IPage对象
     * @author xcx
     */
    public static IPage getPageInfo(int current, int size, List list) {
        IPage iPage = new Page<>(current, size);
        int start = (current - 1) * size;
        int end = current * size;
        if (end > list.size()) {
            end = list.size();
        }
        if (start > list.size()) {
            start = end = list.size();
        }
        List list1 = list.subList(start, end);
        iPage.setRecords(list1);
        iPage.setTotal(list.size());
        return iPage;
    }

}
