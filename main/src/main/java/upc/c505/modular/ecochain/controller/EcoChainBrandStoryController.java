package upc.c505.modular.ecochain.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.entity.EcoChainBrandStory;
import upc.c505.modular.ecochain.service.IEcoChainBrandStoryService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author la
 * @since 2024-09-24
 */
@RestController
@RequestMapping("/eco-chain-brand-story")
@Api(tags = "品牌故事")
public class EcoChainBrandStoryController {
    @Autowired
    IEcoChainBrandStoryService ecoChainBrandStoryService;


    @ApiOperation("新增品牌故事")
    @PostMapping("/insertBrandStory")
    public R insertBrandStory(@RequestBody EcoChainBrandStory ecoChainBrandStory){
        return R.ok(ecoChainBrandStoryService.save(ecoChainBrandStory));
    }

    @ApiOperation("删除品牌故事")
    @PostMapping("/deleteBrandStory")
    public R deleteBrandStory(@RequestBody EcoChainBrandStory ecoChainBrandStory){
        return R.ok(ecoChainBrandStoryService.removeBrandStory(ecoChainBrandStory));
    }
    @ApiOperation("更新品牌故事")
    @PostMapping("/updayeBrandStory")
    public R<Boolean> updateBrandStory(@RequestBody EcoChainBrandStory ecoChainBrandStory){
        boolean result = ecoChainBrandStoryService.updateBrandStory(ecoChainBrandStory);
        return R.ok(result);
    }

    @ApiOperation("根据社会信用代码查询品牌故事，并按type和排序序号排序")
    @PostMapping("/selectSortedBrandStory")
    public R<List<EcoChainBrandStory>> selectSortedBrandStory(@RequestBody EcoChainBrandStory param){
        List<EcoChainBrandStory> list = ecoChainBrandStoryService.selectSortedBrandStory(param);
        return R.ok(list);
    }
}





