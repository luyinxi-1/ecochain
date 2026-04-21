package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.entity.EcoChainProcessTracking;
import upc.c505.modular.ecochain.service.IEcoChainProcessTrackingService;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.filemanage.utils.FileManageUtil;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@RestController
@Api(tags = {"过程追踪"})
@RequestMapping("/eco-chain-process-tracking")
public class EcoChainProcessTrackingController {

    @Autowired
    private IEcoChainProcessTrackingService ecoChainProcessTrackingService;

    @Autowired
    private GetUserInfo getUserInfo;

    @PostMapping("/insertProcessTrackingConfiguration")
    @ApiOperation("新增过程跟踪配置")
    @Transactional
    public R<Void> insertProcessTrackingConfiguration(@RequestBody EcoChainProcessTracking param) {
        ecoChainProcessTrackingService.insertProcessTrackingConfiguration(param);
        return R.ok();
    }

    @PostMapping("/updateProcessTrackingConfiguration")
    @ApiOperation("修改过程跟踪配置")
    public R<Boolean> updateProcessTrackingConfiguration(@RequestBody EcoChainProcessTracking param) {
        boolean result = ecoChainProcessTrackingService.updateProcessTrackingConfiguration(param);
        return R.ok(result);
    }

    /**
     * 根据种植库id查询过程跟踪配置列表
     * @param warehouseId
     * @return
     */
    @PostMapping("/selectListProcessTrackingConfiguration")
    @ApiOperation("查询过程跟踪配置列表")
    public R<List<EcoChainProcessTracking>> selectListProcessTrackingConfiguration(@RequestParam Long warehouseId) {
        List<EcoChainProcessTracking> list = ecoChainProcessTrackingService.listByWarehouseId(warehouseId);
        return R.ok(list);
    }

    @PostMapping("/selectOneProcessTrackingConfiguration")
    @ApiOperation("查询单个过程跟踪配置")
    public R<EcoChainProcessTracking> selectOneProcessTrackingConfiguration(@RequestParam Long id) {
        EcoChainProcessTracking ecoChainProcessTracking = ecoChainProcessTrackingService.getById(id);
        return R.ok(ecoChainProcessTracking);
    }

    @PostMapping("/removeBatchProcessTrackingConfiguration")
    @ApiOperation("批量删除过程跟踪配置")
    public R<Void> removeBatchProcessTrackingConfiguration(@RequestBody List<Long> idList) {
        ecoChainProcessTrackingService.removeBatchProcessTrackingConfiguration(idList);
        return R.ok();
    }

    @PostMapping("/uploadFile")
    @ApiOperation("上传图片文件")
    public R<List<String>> uploadFile(@RequestParam(value = "file") List<MultipartFile> multipartFile) {
        return R.ok(FileManageUtil.uploadFile(multipartFile, "upload/public/eco_chain_enterprises_storage/" + getUserInfo.getSocialCreditCode()));
    }
    @PostMapping("/deleteFile")
    @ApiOperation("删除图片文件")
    public R<Void> deleteFile(@RequestParam String filePath) {
        if(FileManageUtil.deleteFile(filePath))
            return R.ok();
        else
            return R.fail("删除文件失败");
    }
}
