package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.entity.EcoChainAiSimulationReverseAssessment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author la
 * @since 2025-04-16
 */
public interface IEcoChainAiSimulationReverseAssessmentService extends IService<EcoChainAiSimulationReverseAssessment> {

    List<EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam> getNumerByTime(EcoChainAiSimulationReverseAssessmentGetNumberByTimeParam param);

    List<EcoChainAiSimulationReverseAssessmentReturnParam> aiSimulationReverseAssessment(EcoChainAiSimulationReverseAssessmentSearchParam param);

    List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam> getPeopleBasicSalaryAndScore();


    void exportAiSimulationReverseAssessment(HttpServletResponse response, EcoChainAiSimulationReverseAssessmentSearchParam param);

    List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam> updateBasicInfoWithResult(List<EcoChainAiSimulationReverseAssessmentUpdateParam> param);
}
