package upc.c505.modular.filemanage.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 文件管理工具类
 * @CreateTime: 2023-07-28
 */
public class FileManageUtil {
    /**
     * @Description 上传至固定路径   "upload/" + authority +"/" + type + "/" + currentTime
     * @param files 文件列表
     * @param type  文件类型
     * @param authority 文件权限 public/private
     * @return  保存文件的相对路径列表
     */
    public static List<String> uploadFile(List<MultipartFile> files, String type, String authority) {
        List<String> result = new ArrayList<>();
        files.forEach(file -> {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentTime = dateFormat.format(now);
//            file.getOriginalFilename(): 获取源文件的昵称
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                throw new RuntimeException("文件名获取失败");
            }
//            在当前包目录下建upload文件夹，按传入的type进行分类，在一个类下按日期建立文件夹
            File filed = new File("upload/" + authority +"/" + type + "/" + currentTime);
            if (!filed.exists()) {
                boolean mkdirs = filed.mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException("文件路径创建失败");
                }
            }
            System.out.println("文件的绝对路径：" + filed.getAbsolutePath());
//            生成文件名
//            System.currentTimeMillis(): 以毫秒为单位返回当前时间
//            Math.random(): 生成0.0-1.0之间的伪随机 double 值，加1防止生成的是0
//            保留文件的后缀名
            String filename = System.currentTimeMillis() + (int) (1 + Math.random() * 1000) + fileName.substring(fileName.lastIndexOf("."));
            try {
//                transferTo(path): 将文件留copy到path路径上
                file.transferTo(new File(filed.getAbsolutePath(), filename));
            } catch (IOException e) {
                throw new RuntimeException("文件保存失败");
            }
//            返回相对路径
//            注意需要做路径映射，才可以访问到静态资源
            result.add("upload/" + authority +"/" + type + "/" + currentTime + "/" + filename);
        });
        return result;
    }
    /**
     * @Description 上传至指定路径   path + "/" + currentTime/
     * @param files 文件列表
     * @param path  文件保存目录
     * @return  保存文件的相对路径列表
     */
    public static List<String> uploadFile(List<MultipartFile> files, String path) {
        List<String> result = new ArrayList<>();
        files.forEach(file -> {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentTime = dateFormat.format(now);
            String originalFileName = file.getOriginalFilename();   // 获取源文件名
            if (StringUtils.isEmpty(originalFileName)) {
                throw new RuntimeException("文件名获取失败");
            }

            File filed = new File(path + "/"+ currentTime);
            if (!filed.exists()) {
                boolean mkdirs = filed.mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException("文件路径创建失败");
                }
            }
//            生成文件名
//            System.currentTimeMillis(): 以毫秒为单位返回当前时间
//            Math.random(): 生成0.0-1.0之间的伪随机 double 值，加1防止生成的是0
//            保留文件的后缀名
            String filename = System.currentTimeMillis() + (int) (1 + Math.random() * 1000) + originalFileName.substring(originalFileName.lastIndexOf("."));
            try {
                file.transferTo(new File(filed.getAbsolutePath(), filename));   // 将上传的文件保存到指定路径
            } catch (IOException e) {
                throw new RuntimeException("文件保存失败");
            }
//            返回相对路径
//            注意需要做路径映射，才可以访问到静态资源
            result.add(path + "/" + currentTime + "/" + filename);
        });
        return result;
    }

    /**
     * 删除文件下所有内容，但是不删除该文件夹
     *
     * @param path 该文件夹的绝对路径
     * @return true or false
     */
    public static boolean delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        File temp;
        assert tempList != null;
        for (String s : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + s);
            } else {
                temp = new File(path + File.separator + s);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + s);//先删除文件夹里面的文件
                delFolder(path + "/" + s);//再删除空文件夹
            }
        }
        return true;
    }

    /**
     * 删除文件夹
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            java.io.File myFilePath = new java.io.File(folderPath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除单个文件
     * @param filePath
     * @return true or false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * 收集 JSON 字符串
     * @param picturesJson JSON 字符串
     */
    public static List<Map<String, String>> parsePictureList(String picturesJson) {
        if (StringUtils.isBlank(picturesJson)) {
            return new ArrayList<>();
        }
        try {
            return new ObjectMapper().readValue(picturesJson, new TypeReference<List<Map<String, String>>>() {});
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "图片解析失败");
        }
    }


    /**
     * 处理图片更新逻辑
     * @param oldPictures 旧的图片 JSON 字符串
     * @param newPictures 新的图片 JSON 字符串
     */
    public static void handlePictureUpdate(String oldPictures, String newPictures) {
        if (ObjectUtils.isNotEmpty(newPictures)) {
            List<Map<String, String>> newPictureList = FileManageUtil.parsePictureList(newPictures);
            List<Map<String, String>> oldPictureList = FileManageUtil.parsePictureList(oldPictures);

            Set<String> newUrls = newPictureList.stream()
                    .map(p -> p.get("url"))
                    .collect(Collectors.toSet());

            Set<String> oldUrls = oldPictureList.stream()
                    .map(p -> p.get("url"))
                    .collect(Collectors.toSet());

            // 找出要删除的图片
            Set<String> urlsToDelete = oldUrls.stream()
                    .filter(url -> !newUrls.contains(url))
                    .collect(Collectors.toSet());

            // 删除不需要的图片
            for (String url : urlsToDelete) {
                FileManageUtil.deleteFile(url);
            }
        }
    }


    /**
     * 批量删除图片
     * @param picturesList 需要删除的图片 JSON 列表
     */
    public static void handleBatchPictureDelete(List<String> picturesList) {
        if (ObjectUtils.isEmpty(picturesList)) {
            return;
        }

        // 解析 JSON 并提取所有要删除的 URL
        Set<String> urlsToDelete = picturesList.stream()
                .flatMap(pictures -> parsePictureList(pictures).stream())
                .map(p -> p.get("url"))
                .collect(Collectors.toSet());

        // 并行删除
        urlsToDelete.parallelStream().forEach(FileManageUtil::deleteFile);
    }
}
