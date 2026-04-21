package upc.c505.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 文件上传/删除使用的路径前缀
 */
public class FileUploadPrefix {
    /**
     * 可删除的文件路径前缀list
     */
    public static final List<String> DELETABLE_PREFIX = Arrays.asList(
            "upload/private/file", "upload/public/file"
    );
}
