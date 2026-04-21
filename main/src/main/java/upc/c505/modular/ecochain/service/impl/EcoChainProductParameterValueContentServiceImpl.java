package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.entity.EcoChainProductManagerValueContent;
import upc.c505.modular.ecochain.entity.EcoChainProductParameter;
import upc.c505.modular.ecochain.entity.EcoChainProductParameterValueContent;
import upc.c505.modular.ecochain.mapper.EcoChainProductManagerValueContentMapper;
import upc.c505.modular.ecochain.mapper.EcoChainProductParameterValueContentMapper;
import upc.c505.modular.ecochain.service.IEcoChainProductParameterValueContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author byh
 * @since 2024-09-25
 */
@Service
public class EcoChainProductParameterValueContentServiceImpl extends ServiceImpl<EcoChainProductParameterValueContentMapper, EcoChainProductParameterValueContent> implements IEcoChainProductParameterValueContentService {

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private EcoChainProductParameterValueContentMapper ecoChainProductParameterValueContentMapper;

    @Autowired
    private EcoChainProductManagerValueContentMapper ecoChainProductManagerValueContentMapper;

    @Override
    public void insertProductParameterValueContent(EcoChainProductParameterValueContent param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            Integer sortNumber = ecoChainProductParameterValueContentMapper.selectMaxSortNumber(param);
            if (ObjectUtils.isNotEmpty(sortNumber)) {
                sortNumber = sortNumber + 1;
            } else {
                sortNumber = 1;
            }
            param.setSortNumber(sortNumber);
            this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }
    }

    @Override
    public boolean updateProductParameterValueContent(EcoChainProductParameterValueContent param) {
        if (ObjectUtils.isEmpty(param.getOperator()) && ObjectUtils.isNotEmpty(UserUtils.get().getUsername())) {
            param.setOperator(UserUtils.get().getUsername());
        }
        if (ObjectUtils.isEmpty(param.getOperationDatetime())) {
            param.setOperationDatetime(LocalDateTime.now());
        }
//        MyLambdaQueryWrapper<EcoChainProductManagerValueContent> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(param.getId()), EcoChainProductManagerValueContent::getEcoChainProductParameterValueContentId, param.getId());
//        List<EcoChainProductManagerValueContent> ecoChainProductManagerValueContents = ecoChainProductManagerValueContentMapper.selectList(lambdaQueryWrapper);
//        if (ObjectUtils.isNotEmpty(ecoChainProductManagerValueContents)) {
//            for (EcoChainProductManagerValueContent ecoChainProductManagerValueContent : ecoChainProductManagerValueContents) {
//                if(ObjectUtils.isNotEmpty(param.getValueName())){
//                    ecoChainProductManagerValueContent.setValueContent(param.getValueName());
//                }
//                ecoChainProductManagerValueContentMapper.updateById(ecoChainProductManagerValueContent);
//            }
//        }
        return ecoChainProductParameterValueContentMapper.updateProductParameterValueContent(param);
    }

    @Override
    public List<EcoChainProductParameterValueContent> selectProductParameterValueContentList(Long productParameterId) {
        MyLambdaQueryWrapper<EcoChainProductParameterValueContent> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EcoChainProductParameterValueContent::getProductParameterId, productParameterId)
                .orderByAsc(EcoChainProductParameterValueContent::getSortNumber);
        List<EcoChainProductParameterValueContent> list = ecoChainProductParameterValueContentMapper.selectList(lambdaQueryWrapper);
        return list;
    }

    @Override
    public void removeProductParameterValueContent(List<Long> idList) {
        if (ObjectUtils.isNotEmpty(idList)) {
//            for (Long id : idList) {
//                MyLambdaQueryWrapper<EcoChainProductManagerValueContent> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
//                lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(id), EcoChainProductManagerValueContent::getEcoChainProductParameterValueContentId, id);
//                ecoChainProductManagerValueContentMapper.delete(lambdaQueryWrapper);
//            }
            ecoChainProductParameterValueContentMapper.deleteBatchIds(idList);
        }
    }

    @Override
    public boolean updateProductParameterSortNumber(Long id, Integer param) {
        EcoChainProductParameterValueContent currentTag = ecoChainProductParameterValueContentMapper.selectById(id);
        // 根据param决定是向上还是向下调整
        // 0向上，1向下
        boolean isNext = param != 0;
        EcoChainProductParameterValueContent adjacentParameter = getAdjacentParameter(currentTag, isNext);
        if (adjacentParameter == null) {
            return false;
        }
        // 交换sortNumber
        Integer tempSortNumber = currentTag.getSortNumber();
        currentTag.setSortNumber(adjacentParameter.getSortNumber());
        adjacentParameter.setSortNumber(tempSortNumber);
        // 更新数据库
        ecoChainProductParameterValueContentMapper.updateById(currentTag);
        ecoChainProductParameterValueContentMapper.updateById(adjacentParameter);
        return true;
    }

    public EcoChainProductParameterValueContent getAdjacentParameter(EcoChainProductParameterValueContent currentTag, boolean isNext) {
        LambdaQueryWrapper<EcoChainProductParameterValueContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EcoChainProductParameterValueContent::getProductParameterId, currentTag.getProductParameterId());
        queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()), EcoChainProductParameterValueContent::getSocialCreditCode, getUserInfo.getSocialCreditCode());

        // 根据 isNext 决定查询前一条还是后一条记录
        if (isNext) {
            queryWrapper.gt(EcoChainProductParameterValueContent::getSortNumber, currentTag.getSortNumber())
                    .orderByAsc(EcoChainProductParameterValueContent::getSortNumber);
        } else {
            queryWrapper.lt(EcoChainProductParameterValueContent::getSortNumber, currentTag.getSortNumber())
                    .orderByDesc(EcoChainProductParameterValueContent::getSortNumber);
        }

        // 使用 Page 对象限制查询结果为 1 条
        Page<EcoChainProductParameterValueContent> page = new Page<>(1, 1);
        page = ecoChainProductParameterValueContentMapper.selectPage(page, queryWrapper);

        // 获取查询结果
        List<EcoChainProductParameterValueContent> records = page.getRecords();
        if (records.isEmpty()) {
            return null; // 如果没有找到相邻记录，则返回 null
        }
        return records.get(0); // 返回查询到的相邻记录
    }
}
