package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import upc.c505.modular.ecochain.entity.EcoChainHistoryList;
import upc.c505.modular.ecochain.mapper.EcoChainCompleteRecordMapper;
import upc.c505.modular.ecochain.mapper.EcoChainHistoryListMapper;
import upc.c505.modular.ecochain.service.IEcoChainHistoryListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@Service
public class EcoChainHistoryListServiceImpl extends ServiceImpl<EcoChainHistoryListMapper, EcoChainHistoryList> implements IEcoChainHistoryListService {

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private EcoChainHistoryListMapper ecoChainHistoryListMapper;

    @Autowired
    private EcoChainCompleteRecordMapper ecoChainCompleteRecordMapper;

    @Override
    public void insertHistoryList(EcoChainHistoryList param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        else if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }
    }

    @Override
    public Integer countCompleteRecord(Long warehouseId) {
        MyLambdaQueryWrapper<EcoChainCompleteRecord> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EcoChainCompleteRecord::getEcoChainBuildWarehouseId, warehouseId);
        List<EcoChainCompleteRecord> ecoChainHistoryLists = ecoChainCompleteRecordMapper.selectList(lambdaQueryWrapper);
        if(ObjectUtils.isNotEmpty(ecoChainHistoryLists)){
            return ecoChainHistoryLists.size();
        }
        return 0;
    }
}
