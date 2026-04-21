package upc.c505.modular.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;

import upc.c505.modular.auth.controller.param.AuthModelParam;
import upc.c505.modular.auth.controller.param.tree.AuthModelTreeNode;
import upc.c505.modular.auth.entity.SysAuth;
import upc.c505.modular.auth.entity.SysAuthModel;
import upc.c505.modular.auth.mapper.NewSysAuthMapper;
import upc.c505.modular.auth.mapper.NewSysAuthModelMapper;
import upc.c505.modular.auth.service.ISysAuthModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.utils.MyBeanUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限模块表 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Service
public class SysAuthModelServiceImpl extends ServiceImpl<NewSysAuthModelMapper, SysAuthModel> implements ISysAuthModelService {

    @Autowired
    private NewSysAuthModelMapper sysAuthModelMapper;

    @Autowired
    private NewSysAuthMapper sysAuthMapper;

    @Override
    public void addModel(AuthModelParam authModelParam) {
        SysAuthModel sysAuthModel = new SysAuthModel();
        BeanUtils.copyProperties(authModelParam, sysAuthModel);
        sysAuthModelMapper.insert(sysAuthModel);
    }

    @Override
    public void deleteModelsByIdList(List<Integer> idList) {
    //先删除模块
        sysAuthModelMapper.deleteBatchIds(idList);
        //删除模块下的权限
        sysAuthMapper.delete(new MyLambdaQueryWrapper<SysAuth>()
                .in(SysAuth::getAuthModelId, idList)
        );
    }

    @Override
    public void updateModelById(AuthModelParam authModelParam) {
        if (authModelParam.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, ":更新时id不能为空");
        }
        SysAuthModel sysAuthModel = new SysAuthModel();
        BeanUtils.copyProperties(authModelParam, sysAuthModel);
        sysAuthModelMapper.updateById(sysAuthModel);
        // 如果改模块名，把权限对应的auth_model_name也改了
        if(ObjectUtils.isNotNull(authModelParam.getAuthModelName())){
            sysAuthMapper.update(
                new SysAuth().setAuthModelName(authModelParam.getAuthModelName()),
                new LambdaUpdateWrapper<SysAuth>()
                    .eq(SysAuth::getAuthModelId,authModelParam.getId())
            );
        }

    }

    @Override
    public List<AuthModelTreeNode> getModelPage(Long parentId) {
        List<SysAuthModel> topAuthModels = sysAuthModelMapper.selectList(
                new MyLambdaQueryWrapper<SysAuthModel>()
                        .eq(SysAuthModel::getParentId, parentId)
                        .orderBy(true,true,SysAuthModel::getSeq)
        );
        List<SysAuthModel> allAuthModels = sysAuthModelMapper.selectList(
                new MyLambdaQueryWrapper<SysAuthModel>()
                        .orderBy(true,true,SysAuthModel::getSeq)
        );
        List<AuthModelTreeNode> authModelTreeNodeList = topAuthModels.stream().map(item -> {
            AuthModelTreeNode authModelTreeNode = MyBeanUtils.copy(item, new AuthModelTreeNode());
            authModelTreeNode.getChildren(allAuthModels);
            return authModelTreeNode;
        }).collect(Collectors.toList());
        return authModelTreeNodeList;
    }
}
