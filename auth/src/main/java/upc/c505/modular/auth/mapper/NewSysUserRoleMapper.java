package upc.c505.modular.auth.mapper;

import upc.c505.modular.auth.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户绑定角色表 Mapper 接口
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Mapper
public interface NewSysUserRoleMapper extends BaseMapper<SysUserRole> {

}
