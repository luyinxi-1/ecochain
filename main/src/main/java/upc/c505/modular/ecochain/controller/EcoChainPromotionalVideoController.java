package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHousePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWarehouseReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainInsertAndUpdatePromotionalVideo;
import upc.c505.modular.ecochain.controller.param.EcoChainPromotionalVideoSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainPromotionalVideo;
import upc.c505.modular.ecochain.service.IEcoChainProductTagService;
import upc.c505.modular.ecochain.service.IEcoChainPromotionalVideoService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xth
 * @since 2025-02-15
 */
@RestController
@RequestMapping("/eco-chain-promotional-video")
@Api(tags = "宣传视频")
public class EcoChainPromotionalVideoController {

    @Autowired
    private IEcoChainPromotionalVideoService ecoChainPromotionalVideoService;

    @PostMapping("/insertPromotionalVideo")
    @ApiOperation("新增宣传视频")
    public R<Void> insertPromotionalVideo(@RequestBody EcoChainInsertAndUpdatePromotionalVideo param){
        ecoChainPromotionalVideoService.insertPromotionalVideo(param);
        return R.ok();
    }

    @PostMapping("/selectPromotionalVideo")
    @ApiOperation("分页查询库")
    public R<PageBaseReturnParam<EcoChainPromotionalVideo>> selectPromotionalVideo(@RequestBody EcoChainPromotionalVideoSearchParam param) {
        Page<EcoChainPromotionalVideo> page = ecoChainPromotionalVideoService.selectPromotionalVideo(param);
        PageBaseReturnParam<EcoChainPromotionalVideo> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }

    @PostMapping("/selectPublishedPromotionalVideo")
    @ApiOperation("分页查询库(已发布)")
    public R<PageBaseReturnParam<EcoChainPromotionalVideo>> selectPublishedPromotionalVideo(@RequestBody EcoChainPromotionalVideoSearchParam param) {
        Page<EcoChainPromotionalVideo> page = ecoChainPromotionalVideoService.selectPublishedPromotionalVideo(param);
        PageBaseReturnParam<EcoChainPromotionalVideo> p = PageBaseReturnParam.ok(page);
        return R.page(p);
    }

    @PostMapping("/updatePromotionalVideo")
    @ApiOperation("修改宣传视频")
    public R<Void> updatePromotionalVideo(@RequestBody EcoChainInsertAndUpdatePromotionalVideo param){
        ecoChainPromotionalVideoService.updatePromotionalVideo(param);
        return R.ok();
    }

    @PostMapping("/deletePromotionalVideo")
    @ApiOperation("删除宣传视频")
    public R<Void> deletePromotionalVideo(@RequestParam Long id){
        ecoChainPromotionalVideoService.deletePromotionalVideo(id);
        return R.ok();
    }

    @PostMapping("/selectPromotionalVideoById")
    @ApiOperation("根据id查询宣传视频")
    public R<EcoChainPromotionalVideo> selectPromotionalVideoById(@RequestParam Long id){
        EcoChainPromotionalVideo result = ecoChainPromotionalVideoService.selectPromotionalVideoById(id);
        return R.ok(result);
    }

}
