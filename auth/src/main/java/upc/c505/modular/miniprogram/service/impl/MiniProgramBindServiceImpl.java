package upc.c505.modular.miniprogram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.modular.miniprogram.entity.MiniProgramBindEntity;
import upc.c505.modular.miniprogram.mapper.MiniProgramBindMapper;
import upc.c505.modular.miniprogram.param.MiniProgramBindParam;
import upc.c505.modular.miniprogram.param.MiniProgramBindSearchParam;
import upc.c505.modular.miniprogram.param.UpdateMiniProgramBindParam;
import upc.c505.modular.miniprogram.service.IMiniProgramBindService;

import java.util.List;

/**
 * 小程序绑定服务实现类
 */
@Service
public class MiniProgramBindServiceImpl extends ServiceImpl<MiniProgramBindMapper, MiniProgramBindEntity>
        implements IMiniProgramBindService {

    @Override
    public MiniProgramBindEntity create(MiniProgramBindParam param) {
        MiniProgramBindEntity entity = new MiniProgramBindEntity();
        entity.setAppId(param.getAppId());
        entity.setAppSecret(param.getAppSecret());
        entity.setWelcomeMessage(param.getWelcomeMessage());
        entity.setMiniProgramName(param.getMiniProgramName());
        entity.setMiniProgramDesc(param.getMiniProgramDesc());
        entity.setCompanyName(param.getCompanyName());
        entity.setCreditCode(param.getCreditCode());
        entity.setReserveField1(param.getReserveField1());
        entity.setReserveField2(param.getReserveField2());
        entity.setReserveField3(param.getReserveField3());
        entity.setReserveField4(param.getReserveField4());
        entity.setReserveField5(param.getReserveField5());
        entity.setReserveField6(param.getReserveField6());
        entity.setReserveField7(param.getReserveField7());
        entity.setReserveField8(param.getReserveField8());
        entity.setReserveField9(param.getReserveField9());
        entity.setReserveField10(param.getReserveField10());
        entity.setReserveField11(param.getReserveField11());
        entity.setReserveField12(param.getReserveField12());
        entity.setReserveField13(param.getReserveField13());
        entity.setReserveField14(param.getReserveField14());
        entity.setReserveField15(param.getReserveField15());
        entity.setReserveField16(param.getReserveField16());
        entity.setReserveField17(param.getReserveField17());
        entity.setReserveField18(param.getReserveField18());
        entity.setReserveField19(param.getReserveField19());
        entity.setReserveField20(param.getReserveField20());
        this.save(entity);
        return entity;
    }

    @Override
    public MiniProgramBindEntity update(UpdateMiniProgramBindParam param) {
        MiniProgramBindEntity entity = this.getById(param.getId());
        if (entity != null) {
            // 更新appId（如果提供了新的值）
            if (param.getAppId() != null) {
                entity.setAppId(param.getAppId());
            }
            // 更新appSecret（如果提供了新的值）
            if (param.getAppSecret() != null) {
                entity.setAppSecret(param.getAppSecret());
            }
            entity.setWelcomeMessage(param.getWelcomeMessage());
            entity.setMiniProgramName(param.getMiniProgramName());
            entity.setMiniProgramDesc(param.getMiniProgramDesc());
            entity.setCompanyName(param.getCompanyName());
            entity.setCreditCode(param.getCreditCode());
            entity.setReserveField1(param.getReserveField1());
            entity.setReserveField2(param.getReserveField2());
            entity.setReserveField3(param.getReserveField3());
            entity.setReserveField4(param.getReserveField4());
            entity.setReserveField5(param.getReserveField5());
            entity.setReserveField6(param.getReserveField6());
            entity.setReserveField7(param.getReserveField7());
            entity.setReserveField8(param.getReserveField8());
            entity.setReserveField9(param.getReserveField9());
            entity.setReserveField10(param.getReserveField10());
            entity.setReserveField11(param.getReserveField11());
            entity.setReserveField12(param.getReserveField12());
            entity.setReserveField13(param.getReserveField13());
            entity.setReserveField14(param.getReserveField14());
            entity.setReserveField15(param.getReserveField15());
            entity.setReserveField16(param.getReserveField16());
            entity.setReserveField17(param.getReserveField17());
            entity.setReserveField18(param.getReserveField18());
            entity.setReserveField19(param.getReserveField19());
            entity.setReserveField20(param.getReserveField20());
            this.updateById(entity);
        }
        return entity;
    }

    @Override
    public MiniProgramBindEntity getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public MiniProgramBindEntity getByAppId(String appId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<MiniProgramBindEntity>()
                .eq(MiniProgramBindEntity::getAppId, appId)
                .last("limit 1"));
    }

    @Override
    public boolean existsByAppId(String appId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<MiniProgramBindEntity>()
                .eq(MiniProgramBindEntity::getAppId, appId)) > 0;
    }

    @Override
    public PageBaseReturnParam<MiniProgramBindEntity> listPage(MiniProgramBindSearchParam param) {
        Page<MiniProgramBindEntity> page = new Page<>(param.getCurrent(), param.getSize());
        QueryWrapper<MiniProgramBindEntity> queryWrapper = new QueryWrapper<>();
        if (param.getKeyword() != null && !param.getKeyword().isEmpty()) {
            queryWrapper.like("mini_program_name", param.getKeyword())
                    .or()
                    .like("company_name", param.getKeyword());
        }
        Page<MiniProgramBindEntity> resultPage = this.page(page, queryWrapper);
        return PageBaseReturnParam.ok(resultPage);
    }

    @Override
    public Boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
