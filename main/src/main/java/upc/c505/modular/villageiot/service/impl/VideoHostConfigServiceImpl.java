package upc.c505.modular.villageiot.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.villageiot.controller.param.HostConfigPageSearchParam;
import upc.c505.modular.villageiot.entity.VideoHostConfig;
import upc.c505.modular.villageiot.mapper.VideoHostConfigMapper;
import upc.c505.modular.villageiot.service.IVideoHostConfigService;

import java.util.List;
import java.util.Objects;

import static upc.c505.modular.phr.service.impl.PhrPopulationServiceImpl.isCommonUser;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author frd
 * @since 2023-11-04
 */
@Service
public class VideoHostConfigServiceImpl extends ServiceImpl<VideoHostConfigMapper, VideoHostConfig> implements IVideoHostConfigService {

    @Autowired
    private VideoHostConfigMapper videoHostConfigMapper;

    @Autowired
    private ISysAreaService sysAreaService;

    @Override
    public Object insertHostConfig(VideoHostConfig videoHostConfig) {
        //新增时，同一个areaId下不能有多个视频平台
        List<VideoHostConfig> list = videoHostConfigMapper.selectList(new MyLambdaQueryWrapper<VideoHostConfig>().isNotNull(VideoHostConfig::getAreaId));
        for (VideoHostConfig i : list) {
            if (Objects.equals(videoHostConfig.getAreaId(), i.getAreaId())) {
                if (Objects.equals(videoHostConfig.getDeviceType(), i.getDeviceType())) {
                    return "existing";
                }
            }
        }
        videoHostConfigMapper.insert(videoHostConfig);
        return videoHostConfig.getId();
    }

    @Override
    public Object updateHostConfig(VideoHostConfig videoHostConfig) {
        //修改时，同一个areaId下不能有多个视频平台
        List<VideoHostConfig> list = videoHostConfigMapper.selectList(new MyLambdaQueryWrapper<VideoHostConfig>()
                .isNotNull(VideoHostConfig::getAreaId)
                .ne(ObjectUtils.isNotEmpty(videoHostConfig.getId()), VideoHostConfig::getId, videoHostConfig.getId()));
        for (VideoHostConfig i : list) {
            if (Objects.equals(videoHostConfig.getAreaId(), i.getAreaId())) {
                if (Objects.equals(videoHostConfig.getDeviceType(), i.getDeviceType())) {
                    return "existing";
                }
            }
        }
        return videoHostConfigMapper.updateById(videoHostConfig);
    }

    @Override
    public Long deleteOneHostConfig(Long id) {
        videoHostConfigMapper.deleteById(id);
        return id;
    }

    @Override
    public Page<VideoHostConfig> selectPage(HostConfigPageSearchParam param) {
        Integer userType = UserUtils.get().getUserType();
        MyLambdaQueryWrapper<VideoHostConfig> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();

        //当输入的areaId不为空时
        if (ObjectUtils.isNotEmpty(param.getAreaId())) {
            //如果当前用户是普通用户，传入的areaId合法则添加查询条件到lambdaQueryWrapper中，不合法则直接返回空page
            if (isCommonUser(userType)) {
                if (Objects.equals(param.getAreaId(), UserUtils.get().getAreaId())) {
                    lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, param.getAreaId());
                } else {
                    return new Page<>();
                }
            }
            //如果当前用户是管理员用户，传入的areaId合法则添加查询条件到lambdaQueryWrapper，不合法则直接返回空page，flag为0时.eq只查适用区域，flag为1时查适用区域及其管辖区域所有的
            else {
                if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 0) {
                    if (UserUtils.get().getManageAreaIdList().contains(param.getAreaId())) {
                        lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, param.getAreaId());
                    } else {
                        return new Page<>();
                    }
                } else if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                    if (UserUtils.get().getManageAreaIdList().contains(param.getAreaId())) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        if (ObjectUtils.isNotEmpty(areaIdList)) {
                            lambdaQueryWrapper.in(VideoHostConfig::getAreaId, areaIdList);
                        }
                    } else {
                        return new Page<>();
                    }
                }
            }
        }
        //当输入的areaId为空时，如果当前用户缓存中数据不合法，那么直接返回空page
        else {
            if (isCommonUser(userType)) {
                if (ObjectUtils.isEmpty(UserUtils.get().getAreaId())) {
                    return new Page<>();
                }
                //普通用户(企业用户和居民用户和企业人员)只查看自己所在区域的内容
                lambdaQueryWrapper.eq(VideoHostConfig::getAreaId, UserUtils.get().getAreaId());
            } else {
                if (ObjectUtils.isEmpty(UserUtils.get().getManageAreaIdList())) {
                    return new Page<>();
                }
                //管理员用户查看自己管辖区域的数据
                List<Long> list = UserUtils.get().getManageAreaIdList();
                list.remove(UserUtils.get().getAreaId());
                lambdaQueryWrapper.in(VideoHostConfig::getAreaId, list);
            }
        }
        Page<VideoHostConfig> page = videoHostConfigMapper.selectPage(
                new Page<>(param.getCurrent(), param.getSize()),
                lambdaQueryWrapper
                        .orderBy(true, Objects.equals(1, param.getIsAsc()), VideoHostConfig::getAddTime)
        );
        return page;
    }
}
