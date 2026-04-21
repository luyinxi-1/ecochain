package upc.c505.modular.villageiot.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.common.requestparam.PageBaseSearchParam;
import upc.c505.modular.villageiot.controller.param.*;
import upc.c505.modular.villageiot.entity.VideoConfigCommon;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author frd
 * @since 2024-03-25
 */
public interface IVideoRandomInspectionService {

    Page<VideoConfigCommon> getIndex(VideoRandomInspectionIndexSearchParam param);

    JSONObject getPageCamerasByRegions(PageBaseSearchParam pageQuery, String regionIndexCode);

    List<VideoInspectionPointLocationInfoParam> videoInspection(Long id);

    JSONObject getRSA(Long areaId);

    JSONObject getVideoChannelUrl(VideoChannelUrlParam param, Long channelsID, Long streamsID, Long transType, Long transProtocol, Long areaId);

    /**
     * 获取海康视频在线和离线数量
     * 救命，怎么会有这么麻烦的接口，哦我的天呐 orz
     * @return Map中包含两个值，Online为设备在线数量，Offline为设备离线数量
     */
    Map<String, Long> getOnlineAndOfflineNumber(OnlineAndOfflineSearchParam param);

    String generateImageByString(ImageGenerateParam param) throws IOException;

    void refreshGuoBiaoOnlineNumber(Integer commonId);

    String cutPicture(String json);

    Page<ProjectRemoteSupervisionReturnParam> selectProjectRemoteSupervisionPage(ProjectRemoteSupervisionSearchParam param);

    /**
     * 建设单位和市场主体用来判断权限的函数
     * @return 如果是建设单位，返回一个项目编号列表，如果是市场主体，返回社会信用代码
     */
    List<String> judgeAuthority();
}
