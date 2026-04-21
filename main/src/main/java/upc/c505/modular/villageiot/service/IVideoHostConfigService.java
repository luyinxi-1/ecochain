package upc.c505.modular.villageiot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.catalina.startup.HostConfig;
import upc.c505.modular.villageiot.controller.param.HostConfigPageSearchParam;
import upc.c505.modular.villageiot.entity.VideoHostConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frd
 * @since 2023-11-04
 */
public interface IVideoHostConfigService extends IService<VideoHostConfig> {

    Object insertHostConfig(VideoHostConfig videoHostConfig);

    Long deleteOneHostConfig(Long id);

    Page<VideoHostConfig> selectPage(HostConfigPageSearchParam param);

    Object updateHostConfig(VideoHostConfig videoHostConfig);
}
