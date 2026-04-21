package upc.c505.modular.villageiot.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import upc.c505.common.requestparam.PageBaseSearchParam;
import upc.c505.modular.villageiot.controller.param.*;
import upc.c505.modular.villageiot.entity.VideoConfigCommon;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author frd
 * @since 2024-01-19
 */
public interface IVideoConfigCommonService extends IService<VideoConfigCommon> {

    Boolean addOneVideoCommon(VideoConfigAddParam videoConfigAddParam);

    Boolean updateOneVideoCommon(VideoConfigUpdateParam videoConfigUpdateParam);

    /**
     * 这个接口是从区县市场局的项目里面拿过来的，用来获取海康视频流
     *
     * @param pageQuery
     * @return
     */
    JSONObject getHikVedioList(PageBaseSearchParam pageQuery);

    Page<VideoUnitReturnParam> selectVideoUnitPage(VideoUnitSearchParam param);

    void deletePointById(Integer id, List<Integer> pointIds);

    void deleteVideoConfigById(Integer id);

    Page<VideoConfigCommon> selectVideoConfigPage(VideoConfigPageSearchParam param);

    VideoConfigReturnParam selectVideoConfigById(Long id);
}
