package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.entity.EcoChainBrandStory;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import upc.c505.modular.ecochain.mapper.EcoChainBrandStoryMapper;
import upc.c505.modular.ecochain.service.IEcoChainBrandStoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.filemanage.utils.FileManageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import upc.c505.modular.partybuilding.entity.PartyBuildingFourDiscussionItem;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author la
 * @since 2024-09-24
 */
@Service
public class EcoChainBrandStoryServiceImpl extends ServiceImpl<EcoChainBrandStoryMapper, EcoChainBrandStory> implements IEcoChainBrandStoryService {
    @Autowired
    EcoChainBrandStoryMapper ecoChainBrandStoryMapper;

    @Override
    public List<EcoChainBrandStory> selectSortedBrandStory(EcoChainBrandStory param){
        List<EcoChainBrandStory> ecoChainBrandStoryList = ecoChainBrandStoryMapper.selectList(
                new LambdaQueryWrapper<EcoChainBrandStory>()
                        .eq(EcoChainBrandStory::getSocialCreditCode,param.getSocialCreditCode())
                        .orderByAsc(EcoChainBrandStory::getType)
                        .orderByAsc(EcoChainBrandStory::getTypeSortingNumber)
        );
        return ecoChainBrandStoryList;
    }

    @Override
    public boolean updateBrandStory(EcoChainBrandStory param) {
        if (ObjectUtils.isEmpty(param) || param.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }

        EcoChainBrandStory ecoChainBrandStory = this.getById(param.getId());
        if (ecoChainBrandStory == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "记录不存在");
        }
        String newPhoto = "[" + param.getPhoto() + "]";
        String oldPhoto = "[" + ecoChainBrandStory.getPhoto() + "]";
        // 统一处理图片更新
        FileManageUtil.handlePictureUpdate(oldPhoto, newPhoto);

        return this.updateById(param);
    }

    @Override
    public boolean removeBrandStory(EcoChainBrandStory param) {
        if (ObjectUtils.isEmpty(param) || param.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }

        EcoChainBrandStory ecoChainBrandStory = this.getById(param.getId());
        if (ecoChainBrandStory == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "记录不存在");
        }
        if (ObjectUtils.isNotEmpty(ecoChainBrandStory.getPhoto())) {
            try {
                String url = new ObjectMapper().readTree(ecoChainBrandStory.getPhoto()).get("url").asText();
                if(!FileManageUtil.deleteFile(url))
                    throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "图片删除失败");
            } catch (JsonProcessingException e) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "图片解析失败");
            }
        }
        return this.removeById(param.getId());
    }
}
