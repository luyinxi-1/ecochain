package upc.c505.modular.auth.mapper;

import org.apache.ibatis.annotations.Param;
import upc.c505.modular.auth.entity.SysArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 区域表 Mapper 接口
 * </p>
 *
 * @author sxz
 * @since 2023-07-30
 */
@Mapper
public interface NewSysAreaMapper extends BaseMapper<SysArea> {

    /**
     * 获取传入id的子区域idList
     * @param id sysArea表的id
     * @return 子区域及本体的idList
     */
    List<Long> getChildAreaIdList(@Param("id") Long id);
}
