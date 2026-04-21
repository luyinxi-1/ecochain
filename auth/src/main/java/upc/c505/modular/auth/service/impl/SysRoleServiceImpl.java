package upc.c505.modular.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.controller.param.FunctionAuthNode;
import upc.c505.modular.auth.controller.param.UserAuthTree;
import upc.c505.modular.auth.controller.param.tree.AuthNode;
import upc.c505.modular.auth.controller.param.user.GetRolePageParam;
import upc.c505.modular.auth.entity.*;
import upc.c505.modular.auth.mapper.*;
import upc.c505.modular.auth.service.ISysRoleAuthService;
import upc.c505.modular.auth.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.utils.MyBeanUtils;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static upc.c505.modular.auth.controller.constant.UserConst.SECOND_ADMIN;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<NewSysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private NewSysRoleMapper sysRoleMapper;

    @Autowired
    private NewSysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private NewSysRoleAuthMapper sysRoleAuthMapper;

    @Autowired
    private ISysRoleAuthService sysRoleAuthService;

    @Autowired
    private NewSysAuthModelMapper sysAuthModelMapper;

    @Autowired
    private NewSysAuthMapper sysAuthMapper;

    @Autowired
    private NewSysAreaMapper sysAreaMapper;

    @Autowired
    private NewSysUserMapper sysUserMapper;

    @Override
    public void addRole(SysRole sysRole) {
        List<SysRole> sysRoles = sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getRoleCode, sysRole.getRoleCode() + "_" + sysRole.getAreaId())
        );
        if (!CollectionUtils.isEmpty(sysRoles)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "角色编码已存在");
        }
        // 添加一个角色时，拼接上该角色适用区域的areaId
        sysRole.setRoleCode(sysRole.getRoleCode() + "_" + sysRole.getAreaId());
        sysRoleMapper.insert(sysRole);
    }

    @Override
    public Page<SysRole> getRoles(GetRolePageParam getRolePageParam) {
        Page<SysRole> sysRolePage = sysRoleMapper.selectPage(
                new Page<>(getRolePageParam.getCurrent(), getRolePageParam.getSize()),
                new MyLambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getRoleCode, getRolePageParam.getRoleCode())
                        .like(SysRole::getRoleName, getRolePageParam.getRoleName())
                        .eq(SysRole::getAreaId, getRolePageParam.getAreaId())
                        .like(SysRole::getAreaName, getRolePageParam.getAreaName())
                        .eq(SysRole::getIsDefaultRole,getRolePageParam.getIsDefaultRole())
                        .eq(SysRole::getStatus,getRolePageParam.getStatus())
                        .orderBy(true,true,SysRole::getSeq)
        );
        return sysRolePage;
    }

    @Override
    public void updateRoleById(SysRole sysRole) {
        sysRoleMapper.updateById(sysRole);
    }

    @Override
    public void deleteRoles(List<Integer> idList) {
        //先删除用户连表中的角色
        sysUserRoleMapper.delete(new MyLambdaQueryWrapper<SysUserRole>()
                .in(SysUserRole::getRoleId, idList));
        //再删除权限连表中的角色
        sysRoleAuthMapper.delete(new MyLambdaQueryWrapper<SysRoleAuth>()
                .in(SysRoleAuth::getRoleId, idList));
        //最后删除角色表中的信息
        sysRoleMapper.deleteBatchIds(idList);
    }

    @Override
    public List<AuthNode> getRoleAuths(@PathVariable Long roleId) {// 传入的roleId为 将被分配权限的角色 的roleId（被操作角色的）
        /*
        1、新增限制：二级管理员只能查询自己拥有的权限
         */
        // 初始化，if中用到的变量
        List<Long> authIdList2 = new ArrayList<>();
        List<Long> distinctAuthModelIds = new ArrayList<>();

        // 获取当前登录用户类型
        Integer userType = UserUtils.get().getUserType();
        if (Objects.equals(userType, SECOND_ADMIN)) {

            /*
            (1)获取当前登录用户的权限id列表(roleId2)
             */
            // ①获取当前登录用户的userCode（在sysUser表获取userid）
            SysUser sysUser = sysUserMapper.selectOne(
                    new LambdaQueryWrapper<SysUser>()
                            .eq(SysUser::getUserCode, UserUtils.get().getUserCode())
            );
            Long userId = sysUser.getId();
            // ②通过userId获取这个二级管理员的角色，roleId2
            SysUserRole sysUserRole = sysUserRoleMapper.selectOne(
                    new LambdaQueryWrapper<SysUserRole>()
                            .eq(SysUserRole::getUserId, userId)
            );
            Long roleId2 = sysUserRole.getRoleId();
            // ③通过roleId2获取当前登录的二级管理员角色具有的权限  id列表
            authIdList2 = sysRoleAuthMapper.selectList(
                    new LambdaQueryWrapper<SysRoleAuth>()
                            .eq(SysRoleAuth::getRoleId, roleId2)
            ).stream().map(SysRoleAuth::getAuthId).collect(Collectors.toList());
//=========
            /*
            (2).取出当前用户拥有的功能模块id列表(distinctAuthModelIds)
             */
            //① 从 sys_auth 表中查询出对应的 auth_model_id 列表并去重【缺少父节点的权限模块】
            distinctAuthModelIds = sysAuthMapper.selectList(
                            new LambdaQueryWrapper<SysAuth>()
                                    .in(SysAuth::getId, authIdList2)
                    ).stream()
                    .map(SysAuth::getAuthModelId)
                    .distinct()
                    .collect(Collectors.toList());
            //② 将父节点的权限模块塞到distinctAuthModelNames列表中
            // 获取distinctAuthModelNames列表中所有节点的parentId集合，并去重
            List<Long> parentIds = sysAuthModelMapper.selectList(
                            new LambdaQueryWrapper<SysAuthModel>()
                                    .in(SysAuthModel::getId, distinctAuthModelIds)
                    )
                    .stream()
                    .map(SysAuthModel::getParentId)
                    .distinct()
                    .collect(Collectors.toList());
            // 查询所有父节点数据
            List<SysAuthModel> parentModels = sysAuthModelMapper.selectList(
                    new LambdaQueryWrapper<SysAuthModel>()
                            .in(SysAuthModel::getId, parentIds)
            );
            // 提取父节点的id
            List<Long> parentAuthModelIds = parentModels.stream()
                    .map(SysAuthModel::getId)
                    .distinct()
                    .collect(Collectors.toList());
            // 将父节点数据合并到distinctAuthModelIds列表中
            distinctAuthModelIds.addAll(parentAuthModelIds);
        }
//===================================================================================
        /*
        2、原来的代码部分（把所有的权限都查出来了）
         */
        // 查出角色拥有的权限
        List<Long> authIdList = sysRoleAuthMapper.selectList(
                new LambdaQueryWrapper<SysRoleAuth>()
                        .eq(SysRoleAuth::getRoleId, roleId)
        ).stream().map(SysRoleAuth::getAuthId).collect(Collectors.toList());
        // 查出所有权限模块
        List<SysAuthModel> sysAuthModels = sysAuthModelMapper.selectList(
                new LambdaQueryWrapper<SysAuthModel>()
                // 将当前用户拥有的功能模块id列表作为条件，从 SysAuthModel 表中查询出所有权限模块
                .in(Objects.equals(userType, SECOND_ADMIN), SysAuthModel::getId, distinctAuthModelIds)
                .orderBy(true,true,SysAuthModel::getSeq)
        );
        // 新建返回权限数
        List<AuthNode> authNodes = new ArrayList<>();
        for (SysAuthModel item : sysAuthModels) {
            // 取出第一级
            if(Objects.equals(item.getParentId(), 0L)){
                authNodes.add(
                        MyBeanUtils.copy(item,new AuthNode())
                        .setAuthName(item.getAuthModelName())
                        .setType(0)
                );
            }
        }
        // 查出所有用户具有的权限列表
        List<SysAuth> sysAuths = sysAuthMapper.selectList(
                new LambdaQueryWrapper<SysAuth>()
                // 查出当前用户（二级管理员）具有的权限列表
                .in(Objects.equals(userType, SECOND_ADMIN), SysAuth::getId, authIdList2)
                .orderBy(true,true,SysAuth::getSeq)
        );
        ConcurrentMap<Long, List<AuthNode>> authMap = sysAuths.stream()
                .map(sysAuth ->
                        //角色有的权限设置"是否拥有权限"为true
                        MyBeanUtils.copy(sysAuth, new AuthNode())
                                .setType(sysAuth.getAuthType())
                                .setIsHad(authIdList.contains(sysAuth.getId()))
                                .setParentId(sysAuth.getAuthModelId()))
                .collect(Collectors.groupingByConcurrent(AuthNode::getParentId));
        // 遍历第一级后的子节点
        authNodes.forEach(
                item->item.getModelChildren(sysAuthModels,authMap)
        );
        return authNodes;
    }

    @Override
    public List<AuthNode> getRoleAuths2(@PathVariable Long roleId) {
        // 传入的roleId为将被分配权限的角色的roleId
        /*
        1.获取当前登录用户的权限id列表
         */
        // 获取当前登录用户的userCode（在sysUser表获取userid）
        SysUser sysUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUserCode, UserUtils.get().getUserCode())
        );
        Long userId = sysUser.getId();
        // 通过userId获取这个二级管理员的角色，roleId
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        );
        Long roleId2 = sysUserRole.getRoleId();
        // 通过roleId2获取当前登录的二级管理员角色具有的权限  id列表
        List<Long> authIdList2 = sysRoleAuthMapper.selectList(
                new LambdaQueryWrapper<SysRoleAuth>()
                        .eq(SysRoleAuth::getRoleId, roleId2)
        ).stream().map(SysRoleAuth::getAuthId).collect(Collectors.toList());
//===================================================================================
        /*
        2.获取被设置用户当前拥有的权限id列表
         */
        List<Long> authIdList = sysRoleAuthMapper.selectList(
                new LambdaQueryWrapper<SysRoleAuth>()
                        .eq(SysRoleAuth::getRoleId, roleId)
        ).stream().map(SysRoleAuth::getAuthId).collect(Collectors.toList());
//===================================================================================
        /*
        3.取出当前用户拥有的功能模块id列表distinctAuthModelIds
         */
        //（1） 从 sys_auth 表中查询出对应的 auth_model_id 列表并去重【缺少父节点的权限模块】
        List<Long> distinctAuthModelIds = sysAuthMapper.selectList(
                        new LambdaQueryWrapper<SysAuth>()
                                .in(SysAuth::getId, authIdList2)
                ).stream()
                .map(SysAuth::getAuthModelId)
                .distinct()
                .collect(Collectors.toList());
        //（2）将父节点的权限模块塞到distinctAuthModelNames列表中
        // 获取distinctAuthModelNames列表中所有节点的parentId集合，并去重
        List<Long> parentIds = sysAuthModelMapper.selectList(
                        new LambdaQueryWrapper<SysAuthModel>()
                                .in(SysAuthModel::getId, distinctAuthModelIds)
                )
                .stream()
                .map(SysAuthModel::getParentId)
                .distinct()
                .collect(Collectors.toList());
        // 查询所有父节点数据
        List<SysAuthModel> parentModels = sysAuthModelMapper.selectList(
                new LambdaQueryWrapper<SysAuthModel>()
                        .in(SysAuthModel::getId, parentIds)
        );
        // 提取父节点的id
        List<Long> parentAuthModelIds = parentModels.stream()
                .map(SysAuthModel::getId)
                .distinct()
                .collect(Collectors.toList());
        // 将父节点数据合并到distinctAuthModelIds列表中
        distinctAuthModelIds.addAll(parentAuthModelIds);
//===================================================================================
        /*
        4. 将当前用户拥有的功能模块id列表作为条件，从 SysAuthModel 表中查询出所有权限模块
         */
        // 查出所有权限模块
        List<SysAuthModel> sysAuthModels = sysAuthModelMapper.selectList(
                new LambdaQueryWrapper<SysAuthModel>()
                        .in(SysAuthModel::getId, distinctAuthModelIds)
                        .orderBy(true,true,SysAuthModel::getSeq)
        );
        // 新建返回权限数
        List<AuthNode> authNodes = new ArrayList<>();
        for (SysAuthModel item : sysAuthModels) {
            // 取出第一级
            if(Objects.equals(item.getParentId(), 0L)){
                authNodes.add(
                        MyBeanUtils.copy(item,new AuthNode())
                                .setAuthName(item.getAuthModelName())
                                .setType(0)
                );
            }
        }
        // 查出当前用户具有的权限列表
        List<SysAuth> sysAuths = sysAuthMapper.selectList(
                new LambdaQueryWrapper<SysAuth>()
                        .in(SysAuth::getId, authIdList2)
                        .orderBy(true,true,SysAuth::getSeq)
        );
        ConcurrentMap<Long, List<AuthNode>> authMap = sysAuths.stream()
                .map(sysAuth ->
                        //角色有的权限设置"是否拥有权限"为true
                        MyBeanUtils.copy(sysAuth, new AuthNode())
                                .setType(sysAuth.getAuthType())
                                .setIsHad(authIdList.contains(sysAuth.getId()))
                                .setParentId(sysAuth.getAuthModelId()))
                .collect(Collectors.groupingByConcurrent(AuthNode::getParentId));
        // 遍历第一级后的子节点
        authNodes.forEach(
                item->item.getModelChildren(sysAuthModels,authMap)
        );
        return authNodes;
    }

    @Override
    public void updateRoleAuthTree(Long roleId, List<Long> idList) {
        // 查出数据库中对应的权限列表
        List<Long> authIdList = sysRoleAuthMapper.selectList(
                new LambdaQueryWrapper<SysRoleAuth>()
                        .select(SysRoleAuth::getAuthId)
                        .eq(SysRoleAuth::getRoleId, roleId)
        ).stream().map(SysRoleAuth::getAuthId).collect(Collectors.toList());
        // 找出新增的
        List<SysRoleAuth> insertList = new ArrayList<>();
        idList.forEach(item->{
            if (!authIdList.contains(item)) {
                insertList.add(new SysRoleAuth()
                        .setRoleId(roleId)
                        .setAuthId(item)
                );
            }
        });
        //找出删除的
        List<SysRoleAuth> deleteList = new ArrayList<>();
        authIdList.forEach(item -> {
            if (!idList.contains(item)) {
                deleteList.add(new SysRoleAuth()
                        .setRoleId(roleId)
                        .setAuthId(item)
                );
            }
        });
        sysRoleAuthService.saveBatch(insertList);
        //如果是空的就不删除
        if (CollectionUtils.isEmpty(deleteList)) {
            return;
        }
        sysRoleAuthMapper.myDeleteBatch(deleteList);
    }

    @Override
    public UserAuthTree getUserAuthTree() {
        UserAuthTree userAuthTree = new UserAuthTree();
        // 查功能权限
        List<String> roleCodeList = UserUtils.get().getRoleCodeList();
        List<SysRole> sysRoles = sysRoleMapper.selectList(new MyLambdaQueryWrapper<SysRole>()
                .select(SysRole::getId, SysRole::getAreaId)
                .in(SysRole::getRoleCode, roleCodeList));
        // 查用户能查看的区域List
        List<Long> roleAreaIdList = sysRoles.stream().map(SysRole::getAreaId).collect(Collectors.toList());
        // Set用于去重AreaId
        Set<Long> areaIdSet = new HashSet<>();
        roleAreaIdList.forEach(item->{
            List<Long> childAreaIdList = sysAreaMapper.getChildAreaIdList(item);
            areaIdSet.addAll(childAreaIdList);
        });
        List<Long> areaIdList = new ArrayList<>(areaIdSet);

        // 查用户对应的角色idList
        List<Long> sysRoleIdList = sysRoles.stream()
                .map(SysRole::getId)
                .collect(Collectors.toList());
        // 把属于该用户的权限id查出来
        List<Long> userAuthIdList = sysRoleAuthMapper.selectList(
                new LambdaQueryWrapper<SysRoleAuth>()
                        .in(SysRoleAuth::getRoleId, sysRoleIdList)
        ).stream().map(SysRoleAuth::getAuthId).collect(Collectors.toList());
        // 把用户的权限查出来，根据ModelId分成Map
        ConcurrentMap<Long, List<FunctionAuthNode>> concurrentMap =
                sysAuthMapper.selectList(new MyLambdaQueryWrapper<SysAuth>()
                .in(SysAuth::getId, userAuthIdList))
                .stream()
                .map(item -> {
                            return MyBeanUtils.copy(item, new FunctionAuthNode())
                                    .setType(item.getAuthType())
                                    .setParentId(item.getAuthModelId());
                        }
                )
                .collect(Collectors.groupingByConcurrent(FunctionAuthNode::getParentId));
        // 把所有权限模块查出来
        List<SysAuthModel> sysAuthModels = sysAuthModelMapper.selectList(null);
        FunctionAuthNode functionAuthNode = new FunctionAuthNode().setId(0L);
        functionAuthNode.getChildrenNode(sysAuthModels,concurrentMap);

        userAuthTree.setFunctionAuthNode(functionAuthNode);
        userAuthTree.setAreaIdList(areaIdList);
        return userAuthTree;
    }
}
