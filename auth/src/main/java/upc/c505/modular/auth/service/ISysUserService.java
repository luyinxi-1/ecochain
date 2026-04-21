package upc.c505.modular.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import upc.c505.common.UserInfoToRedis;
import upc.c505.common.responseparam.R;
import upc.c505.modular.auth.controller.param.UpdateUserPasswordParam;
import upc.c505.modular.auth.controller.param.WechatLoginParam;
import upc.c505.modular.auth.controller.param.user.SysUserPageSearchParam;
import upc.c505.modular.auth.controller.param.user.UserLoginParam;
import upc.c505.modular.auth.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author sxz
 * @since 2023-08-31
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 添加用户
     * 
     * @return 返回id
     */
    Long insertUser(SysUser sysUser);

    /**
     * 根据usercode更新用户信息
     * 
     * @param sysUser 更新的用户信息
     */
    void updateUserByCode(SysUser sysUser);

    /**
     * 根据id删除用户
     * 
     * @param id 要删除用户的id
     * @return 删除数量
     */
    Integer deleteUserById(Long id);

    /**
     * 根据idList批量删除用户
     * 
     * @param idList 要删除的用户idList
     * @return 删除数量
     */
    Integer deleteUserByIdList(List<Long> idList);

    /**
     * 分页查询用户列表
     * 
     * @param param sysUser分页搜索参数
     * @return 用户列表
     */
    Page<SysUser> selectPage(SysUserPageSearchParam param);

    /**
     * WEB端用户登录
     * 
     * @return 返回登陆成功的token
     */
    String login(UserLoginParam userLogin, HttpServletRequest request);

    /**
     * 小程序端获取sessionid
     */
    String getUserOpenId(String code, String appId);

    /**
     * 小程序登录：绑定自定义登录态
     */
    UserInfoToRedis wechatLogin(WechatLoginParam wechatLoginParam, HttpServletRequest request);

    /**
     * 退出登录。删除token
     */
    void logout(HttpServletRequest httpServletRequest);

    /**
     * 用户修改自己密码
     */
    void updateMyPassword(UpdateUserPasswordParam updateUserPasswordParam);

    /**
     * 根据用户账号查询备注信息
     * 
     * @param userCode
     * @return
     */
    String getRemarkByUserCode(String userCode);
}
