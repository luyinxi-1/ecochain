package upc.c505.modular.miniprogram.service;

import com.baomidou.mybatisplus.extension.service.IService;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.modular.miniprogram.entity.MiniProgramBindEntity;
import upc.c505.modular.miniprogram.param.MiniProgramBindParam;
import upc.c505.modular.miniprogram.param.MiniProgramBindSearchParam;
import upc.c505.modular.miniprogram.param.UpdateMiniProgramBindParam;

/**
 * 小程序绑定服务类
 */
public interface IMiniProgramBindService extends IService<MiniProgramBindEntity> {

    /**
     * 创建小程序绑定
     */
    MiniProgramBindEntity create(MiniProgramBindParam param);

    /**
     * 更新小程序绑定
     */
    MiniProgramBindEntity update(UpdateMiniProgramBindParam param);

    /**
     * 删除小程序绑定
     */
    Boolean deleteById(Long id);

    /**
     * 获取详情
     */
    MiniProgramBindEntity getById(Long id);

    /**
     * 根据AppId获取详情
     */
    MiniProgramBindEntity getByAppId(String appId);

    /**
     * 判断AppId是否存在
     */
    boolean existsByAppId(String appId);

    /**
     * 分页查询
     */
    PageBaseReturnParam<MiniProgramBindEntity> listPage(MiniProgramBindSearchParam param);
}
