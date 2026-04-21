package upc.c505.modular.ecochain.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHouseFinishParam;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import upc.c505.modular.ecochain.entity.EcoChainRegionalConfiguration;
import upc.c505.modular.ecochain.mapper.EcoChainBuildWarehouseMapper;
import upc.c505.modular.ecochain.mapper.EcoChainCompleteRecordMapper;
import upc.c505.modular.ecochain.service.IEcoChainCompleteRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.filemanage.utils.FileManageUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@Service
public class EcoChainCompleteRecordServiceImpl extends ServiceImpl<EcoChainCompleteRecordMapper, EcoChainCompleteRecord> implements IEcoChainCompleteRecordService {

    @Autowired
    private EcoChainCompleteRecordMapper ecoChainCompleteRecordMapper;

    @Autowired
    private EcoChainBuildWarehouseMapper ecoChainBuildWarehouseMapper;

    @Autowired
    private GetUserInfo getUserInfo;

    @Override
    public void insertCompleteRecord(EcoChainCompleteRecord param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (StringUtils.isBlank(param.getSocialCreditCode())) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "社会信用代码为空");
        } else {
            if (ObjectUtils.isEmpty(param.getEcoChainBuildWarehouseId())) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "种植库id为空");
            }
            LocalDateTime now = LocalDateTime.now();
            String userName = "";
            if (ObjectUtils.isNotEmpty(UserUtils.get().getUsername())) {
                userName = UserUtils.get().getUsername();
            }
            ecoChainBuildWarehouseMapper.updateStatusById(param.getEcoChainBuildWarehouseId(), "2", now, userName);
            this.save(param);
        }
    }

    @Override
    public List<EcoChainCompleteRecord> listByWarehouseId(Long warehouseId) {
        MyLambdaQueryWrapper<EcoChainCompleteRecord> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        List<EcoChainCompleteRecord> ecoChainCompleteRecords = new ArrayList<>();
        if (ObjectUtils.isNotNull(warehouseId) && warehouseId != 0) {
            lambdaQueryWrapper.eq(EcoChainCompleteRecord::getEcoChainBuildWarehouseId, warehouseId);
            ecoChainCompleteRecords = ecoChainCompleteRecordMapper.selectList(lambdaQueryWrapper);
        }
        return (ecoChainCompleteRecords!= null) ? ecoChainCompleteRecords : new ArrayList<>();
    }

    @Override
    public void finishWarehouse(EcoChainBuildWareHouseFinishParam param) {
        if (ObjectUtils.isNotEmpty(param.getMarketPricing()) || ObjectUtils.isNotEmpty(param.getWarrantyPeriod()) || ObjectUtils.isNotEmpty(param.getOtherDescription())) {
            ecoChainBuildWarehouseMapper.finishWarehouse(param);
        }

        LocalDateTime now = LocalDateTime.now();
        String userName = "";

        if (ObjectUtils.isNotEmpty(UserUtils.get().getUsername())) {
            userName = UserUtils.get().getUsername();
        }

        if (ObjectUtils.isNotEmpty(param.getEcoChainBuildWarehouseId())) {
            EcoChainBuildWarehouse warehouse = ecoChainBuildWarehouseMapper.selectById(param.getEcoChainBuildWarehouseId());

            // 如果allCompletionDatetime没有被赋值，设置当前时间
            if (warehouse.getAllCompletionDatetime() == null) {
                warehouse.setAllCompletionDatetime(now);
            }

            // 使用提取出来的周期计算方法
            long cycle = calculateCycle(warehouse);
            param.setCycle(cycle);

            // 更新allCompletionDatetime
            updateAllCompletionDatetime(param.getEcoChainBuildWarehouseId(), now);

            // 更新状态
            ecoChainBuildWarehouseMapper.updateStatusById(param.getEcoChainBuildWarehouseId(), "3", now, userName);
        }
    }

    @Override
    public boolean updateCompleteRecord(EcoChainCompleteRecord param) {
        if (ObjectUtils.isEmpty(param) || param.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }

        EcoChainCompleteRecord ecoChainCompleteRecord = this.getById(param.getId());
        if (ecoChainCompleteRecord == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "记录不存在");
        }

        // 统一处理图片更新
        FileManageUtil.handlePictureUpdate(ecoChainCompleteRecord.getPictures(), param.getPictures());

        return this.updateById(param);
    }


    @Override
    public void removeCompleteRecord(List<Long> idList) {
        if (ObjectUtils.isEmpty(idList)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "ID 列表不能为空");
        }

        // 批量查询，减少数据库查询次数
        List<EcoChainCompleteRecord> records = this.listByIds(idList);

        // 提取所有图片 JSON
        List<String> picturesList = records.stream()
                .map(EcoChainCompleteRecord::getPictures)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());

        // 删除图片
        FileManageUtil.handleBatchPictureDelete(picturesList);

        // 批量删除数据库记录
        this.removeByIds(idList);
    }




    private long calculateCycle(EcoChainBuildWarehouse warehouse) {
            LocalDateTime now = LocalDateTime.now();
            long cycle = 0;

            // 判断状态和是否存在完成时间，来计算周期
            if ("3".equals(warehouse.getStatus()) && warehouse.getAllCompletionDatetime() != null) {
                // 如果状态是3且allCompletionDatetime存在，计算结束时间与开始时间的差
                cycle = Duration.between(warehouse.getAddDatetime(), warehouse.getAllCompletionDatetime()).toDays();
            } else {
                // 否则，使用当前时间减去开始时间
                cycle = Duration.between(warehouse.getAddDatetime(), now).toDays();
            }
            return cycle;
        }

    // 更新allCompletionDatetime
    private void updateAllCompletionDatetime(Long ecoChainBuildWarehouseId, LocalDateTime allCompletionDatetime) {
        ecoChainBuildWarehouseMapper.updateAllCompletionDatetime(ecoChainBuildWarehouseId, allCompletionDatetime);
    }


}
