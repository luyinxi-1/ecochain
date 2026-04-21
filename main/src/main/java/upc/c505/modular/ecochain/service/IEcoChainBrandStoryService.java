package upc.c505.modular.ecochain.service;

import upc.c505.modular.ecochain.entity.EcoChainBrandStory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author la
 * @since 2024-09-24
 */
public interface IEcoChainBrandStoryService extends IService<EcoChainBrandStory> {

    List<EcoChainBrandStory> selectSortedBrandStory(EcoChainBrandStory param);

    boolean updateBrandStory(EcoChainBrandStory ecoChainBrandStory);

    boolean removeBrandStory(EcoChainBrandStory ecoChainBrandStory);
}
