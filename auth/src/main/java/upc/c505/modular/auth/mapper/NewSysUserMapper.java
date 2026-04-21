package upc.c505.modular.auth.mapper;

import org.apache.ibatis.annotations.Param;
import upc.c505.modular.auth.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Mapper
public interface NewSysUserMapper extends BaseMapper<SysUser> {

    List<String> selectRoleCodeListByUserCode(@Param("userCode") String userCode);
}
