package upc.c505.modular.filemanage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.common.responseparam.R;
import upc.c505.modular.filemanage.utils.FileManageUtil;

import java.util.List;

@RestController
@RequestMapping("/upload")
@Api(tags = "文件上传")
public class FileUploadController {
    @PostMapping("/publicFileUpload")
    @ApiOperation("公开文件上传-都可访问")
    public R<List<String>> publicFileUpload(@RequestParam(value = "file") List<MultipartFile> multipartFile) {
        return R.ok(FileManageUtil.uploadFile(multipartFile, "file", "public"));
    }

    @PostMapping("/privateFileUpload")
    @ApiOperation("隐私文件上传-需要权限访问")
    public R<List<String>> privateFileUpload(@RequestParam(value = "file") List<MultipartFile> multipartFile) {
        return R.ok(FileManageUtil.uploadFile(multipartFile, "file", "private"));
    }
}
