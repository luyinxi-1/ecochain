package upc.c505.modular.auth.service.impl;

import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
import upc.c505.common.UserInfoToRedis;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.constant.SystemConst;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.controller.constant.UserConst;
import upc.c505.modular.auth.controller.constant.WeChatConst;
import upc.c505.modular.auth.controller.param.UpdateUserPasswordParam;
import upc.c505.modular.auth.controller.param.WechatLoginParam;
import upc.c505.modular.auth.controller.param.user.SysUserPageSearchParam;
import upc.c505.modular.auth.controller.param.user.UserLoginParam;
import upc.c505.modular.auth.entity.*;
import upc.c505.modular.auth.mapper.*;
import upc.c505.modular.auth.service.ISysRoleService;
import upc.c505.modular.auth.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import upc.c505.modular.auth.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.dict.mapper.SysDictDataMapper;
import upc.c505.modular.dict.mapper.SysDictTypeMapper;
import upc.c505.modular.miniprogram.entity.MiniProgramBindEntity;
import upc.c505.modular.miniprogram.service.IMiniProgramBindService;
import upc.c505.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<NewSysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private NewSysUserMapper sysUserMapper;
    @Autowired
    private NewSysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private NewSysUserErrorMapper sysUserErrorMapper;
    @Autowired
    private NewSysUserLoginLogMapper sysUserLoginLogMapper;
    @Autowired
    private NewSysAreaMapper sysAreaMapper;
    @Autowired
    private NewSysRoleMapper sysRoleMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SysDeptAreaMapper sysDeptAreaMapper;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;
    @Autowired
    private IMiniProgramBindService miniProgramBindService;

    @Value("${spring.profiles.active}")
    private String activeArea;

    /**
     * 每日登录最多次数
     */
    private final int MAX_TIMES = 5;
    /**
     * 登录成功标志
     */
    private final int LOGIN_SUCCESS = 1;
    /**
     * 登录失败标志
     */
    private final int LOGIN_FAIL = 5;

    @Override
    @Transactional
    public Long insertUser(SysUser sysUser) {
        List<SysUser> sysUsers = sysUserMapper.selectList(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUserCode, sysUser.getUserCode()));
        if (!CollectionUtils.isEmpty(sysUsers)) {
            throw new BusinessException(BusinessErrorEnum.USER_IS_EXIST);
        }
        if (ObjectUtils.isNotNull(sysUser.getPassword())) {
            sysUser.setPassword(MD5Utils.sha256(sysUser.getPassword()));
        } else {
            sysUser.setPassword(MD5Utils.sha256(UserConst.DEFAULT_PASSWORD));
        }
        sysUser.setRemark("new");
        sysUserMapper.insert(sysUser);
        // 先在areaId表中找到flag的值，只有flag为0时才给这个区域创建一套默认角色
        SysArea sysArea = sysAreaMapper.selectOne(
                new LambdaQueryWrapper<SysArea>()
                        .eq(SysArea::getId, sysUser.getAreaId()));
        // 如果创建的用户为二级管理员，并且这个二级管理员的适用区域的flag为0，那么为这个适用区域生成一套默认角色，默认角色拼接上areaId
        if (sysUser.getUserType() == -2 && sysArea.getFlag() == 0) {
            List<String> defaultRoleCodeList = sysDictDataMapper.selectDictDataByType("defaultRoleCodeType")
                    .stream()
                    .map(SysDictData::getName)
                    .collect(Collectors.toList());
            List<String> roleNames = sysDictDataMapper.selectDictDataByType("defaultRoleNameType")
                    .stream()
                    .map(SysDictData::getName)
                    .collect(Collectors.toList());
            List<String> roleCodes = new ArrayList<>();
            for (String code : defaultRoleCodeList) {
                String newCode = code + "_";
                roleCodes.add(newCode);
            }
            List<SysRole> roleList = new ArrayList<>();
            for (int i = 0; i < roleCodes.size(); i++) {
                SysRole sysRole = new SysRole();
                sysRole.setRoleCode(roleCodes.get(i) + sysUser.getAreaId())
                        .setRoleName(roleNames.get(i))
                        .setAreaId(sysUser.getAreaId())
                        .setAreaName(sysUser.getAreaName())
                        .setSeq(1)
                        .setIsDefaultRole(1)
                        .setStatus(1);
                roleList.add(sysRole);
            }
            sysRoleService.saveBatch(roleList);
            // 修改flag的值为1，表示该适用区域已经有了一套默认角色，以后该区域再创建其他二级管理员时，不重复生成
            sysArea.setFlag(1);
            sysAreaMapper.updateById(sysArea);
        }
        return sysUser.getId();
    }

    @Override
    public void updateUserByCode(SysUser sysUser) {
        sysUser.setOperator(UserUtils.get().getUserCode());
        // 如果更改密码
        // if (StringUtils.hasText(sysUser.getPassword())) {
        // sysUser.setPassword(MD5Utils.sha256(sysUser.getPassword()));
        // }
        if (StringUtils.isNotBlank(sysUser.getPassword())) {
            sysUser.setPassword(MD5Utils.sha256(sysUser.getPassword()));
        }
        sysUserMapper.update(
                sysUser,
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getUserCode, sysUser.getUserCode()));
    }

    @Override
    public Integer deleteUserById(Long id) {
        int delete = sysUserMapper.deleteById(id);
        // 删除用户权限
        sysUserRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, id));
        return delete;
    }

    @Override
    public Integer deleteUserByIdList(List<Long> idList) {
        int deleteBatchIds = sysUserMapper.deleteBatchIds(idList);
        // 删除用户权限
        sysUserRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>()
                        .in(SysUserRole::getUserId, idList));
        return deleteBatchIds;
    }

    @Override
    public Page<SysUser> selectPage(SysUserPageSearchParam param) {
        // List<Long> manageAreaIdList = UserUtils.get().getManageAreaIdList();
        // user type不为5的时候正常查询
        if (param.getUserType() != 5) {
            Page<SysUser> sysUserPage = sysUserMapper.selectPage(
                    new Page<>(param.getCurrent(), param.getSize()),
                    new MyLambdaQueryWrapper<SysUser>()
                            // .in(SysUser::getAreaId, manageAreaIdList)
                            .eq(SysUser::getAreaId, param.getAreaId())
                            .like(SysUser::getAreaName, param.getAreaName())
                            .eq(SysUser::getUserType, param.getUserType())
                            .like(SysUser::getUsername, param.getUsername())
                            .like(SysUser::getUserCode, param.getUserCode())
                            .eq(SysUser::getStatus, param.getStatus())
                            .eq(SysUser::getEnterpriseUserType, param.getEnterpriseUserType())
                            .and(ObjectUtils.isNotNull(param.getSearchParam()), wrapper -> {
                                // 用户名称
                                wrapper.like(ObjectUtils.isNotNull(param.getSearchParam()),
                                        SysUser::getUsername, param.getSearchParam())
                                        .or()
                                        // 账号
                                        .like(ObjectUtils.isNotNull(param.getSearchParam()),
                                                SysUser::getUserCode, param.getSearchParam());
                            })
                            .orderByDesc(SysUser::getOperationDatetime));
            return sysUserPage;
        }
        // user type 为5时查询政府人员和网格员（user type=2、3）
        else {
            Page<SysUser> sysUserPage = sysUserMapper.selectPage(
                    new Page<>(param.getCurrent(), param.getSize()),
                    new MyLambdaQueryWrapper<SysUser>()
                            // .in(SysUser::getAreaId, manageAreaIdList)
                            .eq(SysUser::getAreaId, param.getAreaId())
                            .like(SysUser::getAreaName, param.getAreaName())
                            .in(SysUser::getUserType, Arrays.asList(2, 3))
                            .like(SysUser::getUsername, param.getUsername())
                            .like(SysUser::getUserCode, param.getUserCode())
                            .eq(SysUser::getStatus, param.getStatus())
                            .eq(SysUser::getEnterpriseUserType, param.getEnterpriseUserType())
                            .and(ObjectUtils.isNotNull(param.getSearchParam()), wrapper -> {
                                // 用户名称
                                wrapper.like(ObjectUtils.isNotNull(param.getSearchParam()),
                                        SysUser::getUsername, param.getSearchParam())
                                        .or()
                                        // 账号
                                        .like(ObjectUtils.isNotNull(param.getSearchParam()),
                                                SysUser::getUserCode, param.getSearchParam());
                            })
                            .orderByDesc(SysUser::getOperationDatetime));
            return sysUserPage;
        }

    }

    @Override
    public String login(UserLoginParam userLogin, HttpServletRequest request) {
        SysUser sysUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUserCode, userLogin.getUserCode()));
        if (org.springframework.util.ObjectUtils.isEmpty(sysUser)) {
            throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "用户名或密码错误");
        }
        if (0 == sysUser.getStatus()) {
            throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "该账号已停用,请联系管理员");
        }
        String logInfo = request.getHeader(Header.USER_AGENT.getValue());
        UserAgent userAgent = UserAgentParser.parse(logInfo);
        SysUserLoginLog sysUserLoginLog = new SysUserLoginLog()
                .setLoginIp(IPUtils.getIpAddr(request))
                .setLoginDatetime(LocalDateTime.now())
                .setBrowser(userAgent.getBrowser() + userAgent.getVersion())
                .setOsVersion(userAgent.getPlatform() + userAgent.getOsVersion())
                .setUserCode(userLogin.getUserCode())
                .setUsername(sysUser.getUsername());

        // 查询今日是否登陆上线
        SysUserError sysUserError = sysUserErrorMapper.selectOne(
                new LambdaQueryWrapper<SysUserError>()
                        .eq(SysUserError::getUserCode, userLogin.getUserCode())
                        .between(SysUserError::getErrorTime, DateUtils.getTodayStart(), DateUtils.getTodayEnd()));
        if (!org.springframework.util.ObjectUtils.isEmpty(sysUserError)) {
            if (sysUserError.getError().equals(MAX_TIMES)) {
                sysUserLoginLog.setStatus(LOGIN_FAIL);
                sysUserLoginLog.setOperationalInformation("登录已达上限");
                sysUserLoginLogMapper.insert(sysUserLoginLog);
                throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "：您今日已经尝试登录5次，请明日再试！");
            }
        }
        // 验证密码
        if (!MD5Utils.sha256(userLogin.getPassword()).equals(sysUser.getPassword())) {
            // 记录登录错误信息
            // 如果错误log为空，创建新纪录
            if (org.springframework.util.ObjectUtils.isEmpty(sysUserError)) {
                SysUserError sysUserError1 = new SysUserError();
                sysUserError1.setError(1);
                sysUserError1.setUserCode(sysUser.getUserCode());
                sysUserError1.setAddDatetime(LocalDateTime.now());
                sysUserErrorMapper.insert(sysUserError1);
                // 插入日志
                sysUserLoginLog.setStatus(LOGIN_FAIL);
                sysUserLoginLog.setOperationalInformation("密码错误");
                sysUserLoginLogMapper.insert(sysUserLoginLog);
                throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "：密码错误，可再尝试4次！");
            } else {
                int a = sysUserError.getError() + 1;
                sysUserError.setError(a);
                sysUserErrorMapper.updateById(sysUserError);
                sysUserLoginLog.setStatus(LOGIN_FAIL);
                sysUserLoginLog.setOperationalInformation("密码错误");
                sysUserLoginLogMapper.insert(sysUserLoginLog);
                throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "：密码错误，可再尝试" + (5 - a) + "次！");
            }
        }
        sysUserLoginLog.setStatus(LOGIN_SUCCESS);
        sysUserLoginLog.setOperationalInformation("登录成功");
        sysUserLoginLogMapper.insert(sysUserLoginLog);

        UserInfoToRedis userInfoToRedis = new UserInfoToRedis();
        BeanUtils.copyProperties(sysUser, userInfoToRedis);
        List<String> sysRoleCodeList = sysUserMapper.selectRoleCodeListByUserCode(userLogin.getUserCode());
        userInfoToRedis.setRoleCodeList(sysRoleCodeList);
        // ①若sysRoleCodeList为空，则说明没有给该用户分配角色
        // 若当前用户没有分配角色，则不需要获取其管辖区域，直接登录返回token
        // ★除政府人员外，都通过role获取管辖权限；政府人员不通过role获取管辖权限，而是通过部门获取管辖权限
        if (CollectionUtils.isEmpty(sysRoleCodeList)
                && !Objects.equals(sysUser.getUserType(), UserConst.PEOPLE_GOVERNMENT)) {
            throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "，当前用户未分配角色");
        }
        // ②若sysRoleCodeList不为空，则获取其管辖区域
        // 根据用户类型，判断用户管辖区域权限
        // 超级管理员
        if (Objects.equals(sysUser.getUserType(), UserConst.ADMIN)) {
            List<Long> areaIdList = sysRoleMapper.selectList(
                    new LambdaQueryWrapper<SysRole>()
                            .in(SysRole::getRoleCode, sysRoleCodeList))
                    .stream().map(SysRole::getAreaId).collect(Collectors.toList());
            Set<Long> areaIdSet = new HashSet<>();
            for (Long areaId : areaIdList) {
                List<Long> childAreaIdList = sysAreaMapper.getChildAreaIdList(areaId);
                areaIdSet.addAll(childAreaIdList);
            }
            List<Long> finalAreaIdList = new ArrayList<>(areaIdSet);
            userInfoToRedis.setManageAreaIdList(finalAreaIdList);
        }
        // 二级管理员
        if (Objects.equals(sysUser.getUserType(), UserConst.SECOND_ADMIN)) {
            List<Long> areaIdList = sysRoleMapper.selectList(
                    new LambdaQueryWrapper<SysRole>()
                            .in(SysRole::getRoleCode, sysRoleCodeList))
                    .stream().map(SysRole::getAreaId).collect(Collectors.toList());
            Set<Long> areaIdSet = new HashSet<>();
            for (Long areaId : areaIdList) {
                List<Long> childAreaIdList = sysAreaMapper.getChildAreaIdList(areaId);
                areaIdSet.addAll(childAreaIdList);
            }
            List<Long> finalAreaIdList = new ArrayList<>(areaIdSet);
            userInfoToRedis.setManageAreaIdList(finalAreaIdList);
        }
        // 政府人员
        // 政府人员不通过role获取管辖权限，而是通过部门获取管辖权限
        if (Objects.equals(sysUser.getUserType(), UserConst.PEOPLE_GOVERNMENT)) {
            List<Long> areaIdList = sysDeptAreaMapper.selectList(
                    new LambdaQueryWrapper<SysDeptArea>()
                            .eq(SysDeptArea::getDeptId, sysUser.getDeptId()))
                    .stream().map(SysDeptArea::getAreaId).collect(Collectors.toList());
            userInfoToRedis.setManageAreaIdList(areaIdList);
        }
        // 网格人员
        if (Objects.equals(sysUser.getUserType(), UserConst.PEOPLE_GRIDER)) {
            List<Long> areaIdList = sysRoleMapper.selectList(
                    new LambdaQueryWrapper<SysRole>()
                            .in(SysRole::getRoleCode, sysRoleCodeList))
                    .stream().map(SysRole::getAreaId).collect(Collectors.toList());
            Set<Long> areaIdSet = new HashSet<>();
            for (Long areaId : areaIdList) {
                List<Long> childAreaIdList = sysAreaMapper.getChildAreaIdList(areaId);
                areaIdSet.addAll(childAreaIdList);
            }
            List<Long> finalAreaIdList = new ArrayList<>(areaIdSet);
            userInfoToRedis.setManageAreaIdList(finalAreaIdList);
        }
        String token = UUID.randomUUID().toString().replace("-", "_");
        redisUtils.setCacheObject(token, userInfoToRedis, SystemConst.EXPIRED_TIME, TimeUnit.HOURS);
        return token;
    }

    @Override
    public String getUserOpenId(String code, String appId) {
        log.info("getUserOpenId for appId: " + appId + ", code: " + code);
        String WX_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
        String requestUrl = "";

        if (StringUtils.isNotBlank(appId)) {
            MiniProgramBindEntity miniProgramObj = miniProgramBindService.getByAppId(appId);
            if (miniProgramObj != null) {
                requestUrl = WX_URL.replace("APPID", appId)
                        .replace("SECRET", miniProgramObj.getAppSecret()).replace("CODE", code);
            } else {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未找到该appId的绑定配置");
            }
        } else {
            // 兼容旧逻辑
            // 正式环境
            if (Arrays.asList("wanshan", "wanshan1").contains(activeArea)) {
                requestUrl = WX_URL.replace("APPID", WeChatConst.WANSHAN_APP_ID)
                        .replace("SECRET", WeChatConst.WANSHAN_APP_SECRET).replace("CODE", code);
            }
            // 生态链测试环境
            else if (Arrays.asList("stl").contains(activeArea)) {
                requestUrl = WX_URL.replace("APPID", WeChatConst.SHENGTAILIAN_APP_ID)
                        .replace("SECRET", WeChatConst.SEHNGTAILIAN_APP_SECRET).replace("CODE", code);
            }
            // 测试环境
            else {
                requestUrl = WX_URL.replace("APPID", WeChatConst.APP_ID).replace("SECRET", WeChatConst.APP_SECRET)
                        .replace("CODE", code);
            }
        }

        String returnValue = HttpUtil.get(requestUrl);
        String sessionid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(WeChatConst.WX_SESSION_ID + sessionid, returnValue, SystemConst.EXPIRED_TIME,
                TimeUnit.HOURS);
        JSONObject jsonObject = JSON.parseObject(returnValue);
        if (jsonObject.containsKey("errcode") && jsonObject.getInteger("errcode") != 0) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR,
                    "微信授权失败: " + jsonObject.getString("errmsg"));
        }
        String openid = jsonObject.getString("openid");
        return openid;
    }

    // WxService：从微信中拿到用户的隐私信息时使用
    @Override
    public UserInfoToRedis wechatLogin(WechatLoginParam wechatLoginParam, HttpServletRequest request) {
        // 根据openid和appId判断用户表是否有对应用户
        // 根据openid和appId判断用户表是否有对应用户
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getOpenid, wechatLoginParam.getOpenid());

        // 如果传入了appId，则增加appId的过滤条件
        if (StringUtils.isNotBlank(wechatLoginParam.getAppId())) {
            queryWrapper.eq(SysUser::getAppId, wechatLoginParam.getAppId());
        }

        SysUser sysUser = sysUserMapper.selectOne(queryWrapper.last("limit 1"));
        UserInfoToRedis userInfoToRedis = new UserInfoToRedis();
        // (1)用户存在，直接登录【政府人员、网格人员、基民用户、企业人员、美丽乡村建设者】
        if (ObjectUtils.isNotEmpty(sysUser)) {
            return wxLogin(getManageAreas(userInfoToRedis, sysUser));
        }
        // (2)用户不存在，判断是否传入userCode
        else {
            // ①没传userCode，非工作人员登录【居民用户】
            if (StringUtils.isBlank(wechatLoginParam.getUserCode())) {
                // 工作人员首次登录需输入账号密码
                if (wechatLoginParam.getUserType() == 2 || wechatLoginParam.getUserType() == 3) { // 工作人员账户
                    throw new BusinessException(BusinessErrorEnum.USER_NO, "，工作人员请输入账号密码登录");
                }
                // 居民用户进行注册
                else if (wechatLoginParam.getUserType() == 4) {
                    userInfoToRedis
                            .setUsername("")
                            .setPassword(wechatLoginParam.getPassword())// 此处密码为空（注册居民用户传入的密码为空）
                            .setUserType(wechatLoginParam.getUserType())
                            .setOpenid(wechatLoginParam.getOpenid())
                            .setAppId(wechatLoginParam.getAppId()); // Set AppId from param
                    return wxRegister(userInfoToRedis);
                }
                // 抛异常，企业人员和美丽乡村建设者调用各自的注册接口进行注册
                else {
                    throw new BusinessException(BusinessErrorEnum.USER_NO);
                }
            }
            // ②传入userCode，工作人员首次登陆【政府人员、网格人员】
            else {
                sysUser = sysUserMapper.selectOne(
                        new LambdaQueryWrapper<SysUser>()
                                .eq(SysUser::getUserCode, wechatLoginParam.getUserCode()));
                // 登录校验
                validatePassword(sysUser, wechatLoginParam, request);
                // 绑定openid和appId
                sysUser.setOpenid(wechatLoginParam.getOpenid());
                sysUser.setAppId(wechatLoginParam.getAppId());
                sysUserMapper.updateById(sysUser);
                // 登录（返回登录信息）
                return wxLogin(getManageAreas(userInfoToRedis, sysUser));
            }
        }
    }

    /**
     * 获取管辖区域
     * 
     * @param userInfoToRedis 用户信息
     * @param sysUser         用户表
     * @return 用户信息
     */
    private UserInfoToRedis getManageAreas(UserInfoToRedis userInfoToRedis, SysUser sysUser) {
        // 通过openid查出userCode，使用userCode查询权限
        List<String> sysRoleCodeList = sysUserMapper.selectRoleCodeListByUserCode(sysUser.getUserCode());
        userInfoToRedis.setRoleCodeList(sysRoleCodeList);
        BeanUtils.copyProperties(sysUser, userInfoToRedis);
        // // 若sysRoleCodeList为空，则说明没有给该用户分配角色
        // // 若当前用户没有分配角色，则不需要获取其管辖区域，直接登录返回token
        // if (CollectionUtils.isEmpty(sysRoleCodeList) &&
        // !Objects.equals(sysUser.getUserType(), UserConst.PEOPLE_GOVERNMENT)) {
        // throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "，当前用户未分配角色");
        // }
        // 根据用户类型，判断用户管辖区域权限
        // ★政府人员通过部门获取管辖权限
        if (Objects.equals(sysUser.getUserType(), UserConst.PEOPLE_GOVERNMENT)) {
            List<Long> areaIdList = sysDeptAreaMapper.selectList(
                    new LambdaQueryWrapper<SysDeptArea>()
                            .eq(SysDeptArea::getDeptId, sysUser.getDeptId()))
                    .stream().map(SysDeptArea::getAreaId).collect(Collectors.toList());
            userInfoToRedis.setManageAreaIdList(areaIdList);
            // userInfoToRedis.setUserType(UserConst.PEOPLE_GOVERNMENT);
        }
        // ★网格人员通过role获取管辖权限
        if (Objects.equals(sysUser.getUserType(), UserConst.PEOPLE_GRIDER)) {
            List<Long> areaIdList = sysRoleMapper.selectList(
                    new MyLambdaQueryWrapper<SysRole>()
                            .in(SysRole::getRoleCode, sysRoleCodeList))
                    .stream().map(SysRole::getAreaId).collect(Collectors.toList());
            Set<Long> areaIdSet = new HashSet<>();
            for (Long areaId : areaIdList) {
                List<Long> childAreaIdList = sysAreaMapper.getChildAreaIdList(areaId);
                areaIdSet.addAll(childAreaIdList);
            }
            List<Long> finalAreaIdList = new ArrayList<>(areaIdSet);
            userInfoToRedis.setManageAreaIdList(finalAreaIdList);
            // userInfoToRedis.setUserType(UserConst.PEOPLE_GRIDER);
        }
        // // 企业人员和居民用户不需要管辖区域的权限，直接获取UserType 登录
        // // 企业人员、美丽乡村建设者
        // if (Objects.equals(sysUser.getUserType(), PEOPLE_EMPLOYEE)) {
        // userInfoToRedis.setUserType(PEOPLE_EMPLOYEE);
        // }
        // // 居民用户
        // if (Objects.equals(sysUser.getUserType(), PEOPLE_POPULATION)) {
        // userInfoToRedis.setUserType(PEOPLE_POPULATION);
        // }
        userInfoToRedis.setUserType(sysUser.getUserType());

        return userInfoToRedis;
    }

    /**
     * 密码校验
     * 
     * @param sysUser          用户信息
     * @param wechatLoginParam 登录传入参数
     * @param request          请求对象
     */
    private void validatePassword(SysUser sysUser, WechatLoginParam wechatLoginParam, HttpServletRequest request) {
        if (org.springframework.util.ObjectUtils.isEmpty(sysUser)) {
            throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "用户名或密码错误");
        }
        if (0 == sysUser.getStatus()) {
            throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "该账号已停用，请联系管理员");
        }
        String logInfo = request.getHeader(Header.USER_AGENT.getValue());
        UserAgent userAgent = UserAgentParser.parse(logInfo);
        SysUserLoginLog sysUserLoginLog = new SysUserLoginLog()
                .setLoginIp(IPUtils.getIpAddr(request))
                .setLoginDatetime(LocalDateTime.now())
                .setBrowser(userAgent.getBrowser() + userAgent.getVersion())
                .setOsVersion(userAgent.getPlatform() + userAgent.getOsVersion())
                .setUserCode(wechatLoginParam.getUserCode())
                .setUsername(sysUser.getUsername());
        // 查询今日是否登陆上线
        SysUserError sysUserError = sysUserErrorMapper.selectOne(
                new LambdaQueryWrapper<SysUserError>()
                        .eq(SysUserError::getUserCode, wechatLoginParam.getUserCode())
                        .between(SysUserError::getErrorTime, DateUtils.getTodayStart(), DateUtils.getTodayEnd()));
        if (!ObjectUtils.isEmpty(sysUserError)) {
            if (sysUserError.getError().equals(MAX_TIMES)) {
                sysUserLoginLog.setStatus(LOGIN_FAIL);
                sysUserLoginLog.setOperationalInformation("登录已达上限");
                sysUserLoginLogMapper.insert(sysUserLoginLog);
                throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "您今日已经尝试登录5次，请明日再试！");
            }
        }
        // 验证密码
        if (!MD5Utils.sha256(wechatLoginParam.getPassword()).equals(sysUser.getPassword())) {
            // 记录登录错误信息
            // 如果错误log为空，创建新纪录
            if (org.springframework.util.ObjectUtils.isEmpty(sysUserError)) {
                SysUserError sysUserError1 = new SysUserError();
                sysUserError1.setError(1);
                sysUserError1.setUserCode(sysUser.getUserCode());
                sysUserError1.setAddDatetime(LocalDateTime.now());
                sysUserErrorMapper.insert(sysUserError1);
                // 插入日志
                sysUserLoginLog.setStatus(LOGIN_FAIL);
                sysUserLoginLog.setOperationalInformation("密码错误");
                sysUserLoginLogMapper.insert(sysUserLoginLog);
                throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "密码错误，可再尝试4次！");
            } else {
                int a = sysUserError.getError() + 1;
                sysUserError.setError(a);
                sysUserErrorMapper.updateById(sysUserError);
                sysUserLoginLog.setStatus(LOGIN_FAIL);
                sysUserLoginLog.setOperationalInformation("密码错误");
                sysUserLoginLogMapper.insert(sysUserLoginLog);
                throw new BusinessException(BusinessErrorEnum.LOGIN_FAIL, "密码错误，可再尝试" + (5 - a) + "次！");
            }
        }
        sysUserLoginLog.setStatus(LOGIN_SUCCESS);
        sysUserLoginLog.setOperationalInformation("登录成功");
        sysUserLoginLogMapper.insert(sysUserLoginLog);
    }

    /**
     * 登录（返回登录信息）
     * 
     * @param userInfoToRedis 登录信息
     * @return 登录信息
     */
    private UserInfoToRedis wxLogin(UserInfoToRedis userInfoToRedis) {
        String token = UUID.randomUUID().toString().replace("-", "_");
        userInfoToRedis.setToken(token);
        redisUtils.setCacheObject(token, userInfoToRedis, SystemConst.EXPIRED_TIME, TimeUnit.HOURS);
        return userInfoToRedis;
    }

    /**
     * 注册
     * 
     * @param userInfoToRedis 登录信息
     * @return 登录信息
     */
    private UserInfoToRedis wxRegister(UserInfoToRedis userInfoToRedis) {
        // 注册用户信息，openid绑定用户信息
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userInfoToRedis, sysUser);
        // 为用户随机生成账号
        String uuidUserCode = UUID.randomUUID().toString().replace("-", "");
        sysUser.setUserCode(uuidUserCode);
        sysUser.setRemark("new");
        sysUser.setAppId(userInfoToRedis.getAppId()); // Save AppId
        sysUserMapper.insert(sysUser);
        userInfoToRedis
                .setId(sysUser.getId())
                .setUserCode(sysUser.getUserCode());
        return wxLogin(userInfoToRedis);
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(SystemConst.TOKEN_NAME);
        redisUtils.deleteObject(token);
        UserUtils.clear();
    }

    @Override
    public void updateMyPassword(UpdateUserPasswordParam updateUserPasswordParam) {
        String password = sysUserMapper.selectOne(new MyLambdaQueryWrapper<SysUser>()
                .select(SysUser::getPassword)
                .eq(SysUser::getUserCode, updateUserPasswordParam.getUserCode()))
                .getPassword();
        if (!MD5Utils.sha256(updateUserPasswordParam.getOldPassword()).equals(password)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "：旧密码输入错误");
        }
        SysUser sysUser = new SysUser()
                .setPassword(MD5Utils.sha256(updateUserPasswordParam.getNewPassword()))
                .setRemark("");
        sysUserMapper.update(
                sysUser,
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getUserCode, updateUserPasswordParam.getUserCode()));
        // 调用修改密码接口后，remark字段变为空，表示新用户已经完成修改密码操作
    }

    @Override
    public String getRemarkByUserCode(String userCode) {
        if (StringUtils.isNotBlank(userCode)) {
            SysUser sysUser = this.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUserCode, userCode));

            if (sysUser != null && StringUtils.isNotBlank(sysUser.getRemark())) {
                return sysUser.getRemark();
            }
        }
        return "";
    }
}
