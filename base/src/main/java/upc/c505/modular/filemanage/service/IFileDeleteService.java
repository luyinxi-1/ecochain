package upc.c505.modular.filemanage.service;

import upc.c505.common.responseparam.R;

import java.util.List;

public interface IFileDeleteService {
    /**
     * 删除指定的文件
     * 请求路径示例："upload/private/file/20220715/xxx.jpg"
     * 只可删除指定路径前缀下的文件，路径前缀在FileUploadPrefix中添加
     *
     * @param pathList 文件路径list
     * @return 删除成功的数量
     */
    R<String> specifiedFilesDelete(List<String> pathList);

    /**
     * 删除指定的文件夹，文件夹下内容全部删除
     * 请求路径示例："upload/private/file/20220715"
     * 只可删除指定路径前缀下的文件，路径前缀在FileUploadPrefix中添加
     *
     * @param pathList 文件夹路径list
     * @return 删除成功的数量
     */
    R<String> specifiedFoldersDelete(List<String> pathList);
}
