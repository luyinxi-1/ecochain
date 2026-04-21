package upc.c505.modular.villageiot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.modular.villageiot.controller.param.InspectionAnalysisReturnParam;
import upc.c505.modular.villageiot.controller.param.InspectionAnalysisSearchParam;
import upc.c505.modular.villageiot.controller.param.InspectionRecordPageSearchParam;
import upc.c505.modular.villageiot.entity.VideoInspectionRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frd
 * @since 2024-06-28
 */
public interface IVideoInspectionRecordService extends IService<VideoInspectionRecord> {

    Long sendWarning(VideoInspectionRecord videoInspectionRecord);

    Page<VideoInspectionRecord> selectInspectionRecordPage(InspectionRecordPageSearchParam param);

    List<InspectionAnalysisReturnParam> inspectionAnalysis(InspectionAnalysisSearchParam param);

    void generateInspectionBill(Long id, HttpServletResponse response);

    Map<String, Integer> countRemoteSupervision(List<String> projectNumberList);
}
