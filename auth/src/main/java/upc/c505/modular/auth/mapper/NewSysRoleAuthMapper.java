package upc.c505.modular.auth.mapper;

import org.apache.ibatis.annotations.Param;
import upc.c505.modular.auth.entity.SysRoleAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色绑定权限表 Mapper 接口
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Mapper
public interface NewSysRoleAuthMapper extends BaseMapper<SysRoleAuth> {

    void myDeleteBatch(@Param("deleteList") List<SysRoleAuth> deleteList);
}
