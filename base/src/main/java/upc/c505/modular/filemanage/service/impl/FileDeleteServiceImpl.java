package upc.c505.modular.filemanage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.stereotype.Service;
import upc.c505.common.responseparam.R;
import upc.c505.constant.FileUploadPrefix;
import upc.c505.modular.filemanage.service.IFileDeleteService;
import upc.c505.modular.filemanage.utils.FileManageUtil;
import upc.c505.modular.filemanage.utils.TargetIndexUtil;

import java.io.File;
import java.util.List;

@Service
public class FileDeleteServiceImpl implements IFileDeleteService {
    @Override
    public R<String> specifiedFilesDelete(List<String> pathList) {
        assert CollectionUtils.isEmpty(pathList) : "待删除的文件路径不能为空";
//        判断路径，不可以乱删
        for (String filePath : pathList) {
//            取出路径path的第三个"/"前的所有内容，判断是否在设置的前缀中。防止误删除其他内容
            if (!FileUploadPrefix.DELETABLE_PREFIX.contains(filePath.substring(0, TargetIndexUtil.indexOfTarget(filePath,"/",3)))) {
                throw new RuntimeException("文件路径不可删除");
            }
        }
//        开始删除
        int successNum = 0;
        for (String path : pathList) {
            File deleteFile = new File(path);
            if (deleteFile.exists()) {
                boolean delete = deleteFile.delete();
                if (!delete) {
                    throw new RuntimeException("文件删除失败");
                }
                successNum += 1;
            }
        }
        return R.ok("成功删除 " + successNum + " 个");
    }

    @Override
    public R<String> specifiedFoldersDelete(List<String> pathList) {
        assert CollectionUtils.isEmpty(pathList) : "待删除的文件夹路径不能为空";
//        判断路径，不可以乱删
        for (String folderPath : pathList) {
//            取出路径path的第三个"/"前的所有内容，判断是否在设置的前缀中。防止误删除其他内容
            if (!FileUploadPrefix.DELETABLE_PREFIX.contains(folderPath.substring(0, TargetIndexUtil.indexOfTarget(folderPath,"/",3)))) {
                throw new RuntimeException("文件夹路径不可删除");
            }
        }
//        开始删除
        int successNum = 0;
        for (String folder : pathList) {
            File deleteFolder = new File(folder);
            if (deleteFolder.exists()) {
                boolean fileDelete = FileManageUtil.delAllFile(deleteFolder.getAbsolutePath());
                if (!fileDelete) {
                    throw new RuntimeException("文件删除失败");
                }
//                删除文件夹
                boolean folderDelete = deleteFolder.delete();
                if (!folderDelete) {
                    throw new RuntimeException("文件夹删除失败");
                }
                successNum += 1;
            }
        }
        return R.ok("成功删除 " + successNum + " 个");
    }
}
