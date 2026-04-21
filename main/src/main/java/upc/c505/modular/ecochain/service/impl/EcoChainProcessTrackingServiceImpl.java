package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.c505.common.UserUtils;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import upc.c505.modular.ecochain.entity.EcoChainProcessTracking;
import upc.c505.modular.ecochain.mapper.EcoChainBuildWarehouseMapper;
import upc.c505.modular.ecochain.mapper.EcoChainProcessTrackingMapper;
import upc.c505.modular.ecochain.service.IEcoChainProcessTrackingService;
import upc.c505.modular.filemanage.utils.FileManageUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class EcoChainProcessTrackingServiceImpl extends ServiceImpl<EcoChainProcessTrackingMapper, EcoChainProcessTracking> implements IEcoChainProcessTrackingService {

    @Autowired
    private EcoChainBuildWarehouseMapper ecoChainBuildWarehouseMapper;

    @Override
    public boolean insertProcessTrackingConfiguration(EcoChainProcessTracking param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (StringUtils.isBlank(param.getSocialCreditCode())) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "社会信用代码为空");
        } else {
            if(ObjectUtils.isNotEmpty(param.getEcoChainBuildWarehouseId())){
                EcoChainBuildWarehouse ecoChainBuildWarehouse = ecoChainBuildWarehouseMapper.selectById(param.getEcoChainBuildWarehouseId());
                if (Objects.equals(ecoChainBuildWarehouse.getStatus(), "0") || ecoChainBuildWarehouse.getStatus() == null || ecoChainBuildWarehouse.getStatus().equals("")) {
                    LocalDateTime now = LocalDateTime.now();
                    String userName = "";
                    if (ObjectUtils.isNotEmpty(UserUtils.get().getUsername())) {
                        userName = UserUtils.get().getUsername();
                    }
                    ecoChainBuildWarehouseMapper.updateStatusById(param.getEcoChainBuildWarehouseId(), "1", now, userName);
                }
            }
            return this.save(param);
        }
    }

    /**
     * 根据种植库id查询过程跟踪配置列表
     * @param warehouseId
     * @return
     */
    @Override
    public List<EcoChainProcessTracking> listByWarehouseId(Long warehouseId) {
        LambdaQueryWrapper<EcoChainProcessTracking> queryWrapper = new LambdaQueryWrapper<>();

        List<EcoChainProcessTracking> list = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(warehouseId) && warehouseId != 0) {
            queryWrapper.eq(EcoChainProcessTracking::getEcoChainBuildWarehouseId, warehouseId);
            list = this.list(queryWrapper);
        }

        return (list != null) ? list : new ArrayList<>();

    }

    @Override
    public boolean updateProcessTrackingConfiguration(EcoChainProcessTracking param) {
        if (ObjectUtils.isEmpty(param) || param.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }

        EcoChainProcessTracking ecoChainProcessTracking = this.getById(param.getId());
        if (ecoChainProcessTracking == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "记录不存在");
        }

        // 统一处理图片更新
        FileManageUtil.handlePictureUpdate(ecoChainProcessTracking.getProcessPictures(), param.getProcessPictures());

        return this.updateById(param);
    }

    @Override
    public void removeBatchProcessTrackingConfiguration(List<Long> idList) {
        if (ObjectUtils.isEmpty(idList)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "ID 列表不能为空");
        }

        // 批量查询，减少数据库查询次数
        List<EcoChainProcessTracking> records = this.listByIds(idList);

        // 提取所有图片 JSON
        List<String> picturesList = records.stream()
                .map(EcoChainProcessTracking::getProcessPictures)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());

        // 删除图片
        FileManageUtil.handleBatchPictureDelete(picturesList);

        // 批量删除数据库记录
        this.removeByIds(idList);

    }
}
