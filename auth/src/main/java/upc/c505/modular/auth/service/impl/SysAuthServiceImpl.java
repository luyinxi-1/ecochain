package upc.c505.modular.auth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.controller.param.user.AuthParam;
import upc.c505.modular.auth.controller.param.user.GetAuthPageParam;
import upc.c505.modular.auth.entity.SysAuth;
import upc.c505.modular.auth.entity.SysRoleAuth;
import upc.c505.modular.auth.mapper.NewSysAuthMapper;
import upc.c505.modular.auth.mapper.NewSysRoleAuthMapper;
import upc.c505.modular.auth.service.ISysAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Service
public class SysAuthServiceImpl extends ServiceImpl<NewSysAuthMapper, SysAuth> implements ISysAuthService {

    @Autowired
    private NewSysAuthMapper sysAuthMapper;

    @Autowired
    private NewSysRoleAuthMapper sysRoleAuthMapper;

    @Override
    public void addAuth(AuthParam authParam) {
        List<SysAuth> sysAuths = sysAuthMapper.selectList(
                new MyLambdaQueryWrapper<SysAuth>()
                        .eq(SysAuth::getUrl, authParam.getUrl())
                        .eq(SysAuth::getAuthModelId,authParam.getAuthModelId())
        );
        if (CollectionUtils.isNotEmpty(sysAuths)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, ":已存在url相同的权限点");
        }
        SysAuth sysAuth = new SysAuth();
        BeanUtils.copyProperties(authParam, sysAuth);
        sysAuth.setId(null);
        // 存authModelName;
        // 程序能运行到这部说明sysAuths一定为空，所以下面的get(0)一定报错
//        sysAuth.setAuthModelName(sysAuths.get(0).getAuthModelName());
        sysAuthMapper.insert(sysAuth);
    }

    @Override
    public PageBaseReturnParam<SysAuth> getAuths(GetAuthPageParam getAuthPageParam) {
        Page<SysAuth> authGetPage = new Page<>(getAuthPageParam.getCurrent(), getAuthPageParam.getSize());
        Page<SysAuth> sysAuthPageReturn = sysAuthMapper.selectPage(authGetPage,
                new MyLambdaQueryWrapper<SysAuth>()
                        .eq(SysAuth::getAuthModelId, getAuthPageParam.getAuthModelId())
                        .like(SysAuth::getAuthModelName,getAuthPageParam.getAuthModelName())
                        .like(SysAuth::getAuthName, getAuthPageParam.getAuthName())
                        .eq(SysAuth::getSeq, getAuthPageParam.getSeq())
                        .eq(SysAuth::getStatus, getAuthPageParam.getStatus())
                        .eq(SysAuth::getAuthType, getAuthPageParam.getAuthType())
                        .eq(SysAuth::getUrl, getAuthPageParam.getUrl())
        );
//        PageBaseReturnParam<AuthParam> authParamPageBaseReturnParam = new PageBaseReturnParam<AuthParam>()
//                .setPageNo(sysAuthPageReturn.getCurrent())
//                .setTotal(sysAuthPageReturn.getTotal())
//                .setData(sysAuthPageReturn.getRecords().stream()
//                        .map(item -> {
//                            AuthParam authParam = new AuthParam();
//                            BeanUtils.copyProperties(item, authParam);
//                            return authParam;
//                        })
//                        .collect(Collectors.toList())
//                );
        PageBaseReturnParam<SysAuth> p = PageBaseReturnParam.ok(sysAuthPageReturn);
        return p;
    }

    @Override
    public void deleteAuths(List<Integer> idList) {
        //先删除连表中的数据
        sysRoleAuthMapper.delete(new MyLambdaQueryWrapper<SysRoleAuth>()
                .in(SysRoleAuth::getAuthId, idList));
        //然后删除sys_auth中的数据
        sysAuthMapper.deleteBatchIds(idList);
    }

    @Override
    public void updateByAuthId(AuthParam authParam) {
        if (authParam.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, ":更新时id不能为空");
        }
        SysAuth sysAuth = new SysAuth();
        BeanUtils.copyProperties(authParam, sysAuth);
        sysAuthMapper.updateById(sysAuth);
    }
}
