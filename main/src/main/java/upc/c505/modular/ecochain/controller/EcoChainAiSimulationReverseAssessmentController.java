package upc.c505.modular.ecochain.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.controller.param.EcoChainAiSimulationReverseAssessmentUpdateParam;
import upc.c505.modular.ecochain.service.IEcoChainAiSimulationReverseAssessmentService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author la
 * @since 2025-04-16
 */
@RestController
@RequestMapping("/eco-chain-ai-simulation-reverse-assessment")
@Api(tags = "AI模拟反向测评")
public class EcoChainAiSimulationReverseAssessmentController {
    @Autowired
    private IEcoChainAiSimulationReverseAssessmentService ecoChainAiSimulationReverseAssessmentService;

    @PostMapping("/getNumerByTime")
    @ApiOperation("查询人员每过程跟踪数和完成记录数")
    public R<List<EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam>> getNumerByTime(@RequestBody EcoChainAiSimulationReverseAssessmentGetNumberByTimeParam param){
        List<EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam> resultList = ecoChainAiSimulationReverseAssessmentService.getNumerByTime(param);
        return R.ok(resultList);
    }

    @PostMapping("/aiSimulationReverseAssessment")
    @ApiOperation("AI模拟反向测评")
    public R<List<EcoChainAiSimulationReverseAssessmentReturnParam>> aiSimulationReverseAssessment(@RequestBody EcoChainAiSimulationReverseAssessmentSearchParam param){
        List<EcoChainAiSimulationReverseAssessmentReturnParam> resultList = ecoChainAiSimulationReverseAssessmentService.aiSimulationReverseAssessment(param);
        return R.ok(resultList);
    }

    @PostMapping("/exportAiSimulationReverseAssessment")
    @ApiOperation("导出AI模拟反向测评")
    public void exportAiSimulationReverseAssessment(HttpServletResponse response, @RequestBody EcoChainAiSimulationReverseAssessmentSearchParam param) {
        ecoChainAiSimulationReverseAssessmentService.exportAiSimulationReverseAssessment(response, param);
    }


    @GetMapping("/getPeopleBasicSalaryAndScore")
    @ApiOperation("查询该公司下所有人员基础工资和基础分")
    public R<List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam>> getPeopleBasicSalaryAndScore(){
        List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam> resultList = ecoChainAiSimulationReverseAssessmentService.getPeopleBasicSalaryAndScore();
        return R.ok(resultList);
    }
    @PostMapping("/updatePeopleBasicInfo")
    @ApiOperation("修改人员基础工资和基础分")
    public R<List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam>> updatePeopleBasicInfo(@RequestBody List<EcoChainAiSimulationReverseAssessmentUpdateParam> param) {
        List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam> result = ecoChainAiSimulationReverseAssessmentService.updateBasicInfoWithResult(param);
        return R.ok(result);
    }

}
