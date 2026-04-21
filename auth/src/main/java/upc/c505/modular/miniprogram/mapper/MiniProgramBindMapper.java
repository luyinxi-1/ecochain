package upc.c505.modular.miniprogram.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import upc.c505.modular.miniprogram.entity.MiniProgramBindEntity;

import java.util.List;

/**
 * 小程序绑定Mapper接口
 */
public interface MiniProgramBindMapper extends BaseMapper<MiniProgramBindEntity> {
    /**
     * 根据关键词模糊查询小程序绑定信息
     * 
     * @param keyword 关键词
     * @return 查询结果列表
     */
    List<MiniProgramBindEntity> selectByKeyword(@Param("keyword") String keyword);
}
