package upc.c505.modular.ecochain.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.common.UserUtils;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.entity.SysUser;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.auth.service.ISysUserService;
import upc.c505.modular.ecochain.controller.listener.EcoChainBuildWarehouseImportListener;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWareHousePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainBuildWarehouseReturnParam;
import upc.c505.modular.ecochain.controller.param.excelparam.EcoChainBuildWarehouseImportParam;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import upc.c505.modular.ecochain.entity.EcoChainProcessTracking;
import upc.c505.modular.ecochain.entity.EcoChainRegionalConfiguration;
import upc.c505.modular.ecochain.entity.EcoChainTypeConfiguration;
import upc.c505.modular.ecochain.mapper.EcoChainBuildWarehouseMapper;
import upc.c505.modular.ecochain.mapper.EcoChainCompleteRecordMapper;
import upc.c505.modular.ecochain.mapper.EcoChainProcessTrackingMapper;
import upc.c505.modular.ecochain.service.IEcoChainBuildWarehouseService;
import upc.c505.modular.ecochain.service.IEcoChainProcessTrackingService;
import upc.c505.modular.ecochain.service.IEcoChainCompleteRecordService;
import upc.c505.modular.ecochain.service.IEcoChainRegionalConfigurationService;
import upc.c505.modular.ecochain.service.IEcoChainTypeConfigurationService;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.filemanage.utils.FileManageUtil;
import upc.c505.modular.people.entity.PeopleEnterprise;
import upc.c505.modular.people.service.IPeopleEnterpriseService;
import upc.c505.modular.people.service.IPeopleGovernmentService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xth
 * @since 2024-09-21
 */
@Slf4j
@Service
public class EcoChainBuildWarehouseServiceImpl extends ServiceImpl<EcoChainBuildWarehouseMapper, EcoChainBuildWarehouse> implements IEcoChainBuildWarehouseService {

    @Autowired
    private EcoChainBuildWarehouseMapper ecoChainBuildWarehouseMapper;

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private IEcoChainCompleteRecordService ecoChainCompleteRecordService;

    @Autowired
    private EcoChainCompleteRecordMapper ecoChainCompleteRecordMapper;

    @Autowired
    private EcoChainProcessTrackingMapper ecoChainProcessTrackingMapper;

    @Autowired
    private IEcoChainProcessTrackingService ecoChainProcessTrackingService;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IEcoChainTypeConfigurationService typeConfigurationService;

    @Autowired
    private IEcoChainRegionalConfigurationService regionalConfigurationService;

    @Autowired
    private IPeopleEnterpriseService peopleEnterpriseService;

    @Override
    public void insertBuildWarehouse(EcoChainBuildWarehouse param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            if(ObjectUtils.isEmpty(param.getStatus())){
                param.setStatus("0");
            }
            if (ObjectUtils.isNotEmpty(UserUtils.get().getId())){
                param.setCreatorId(UserUtils.get().getId());
            }

            // 生成批号batchNumber
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String dateString = currentDate.format(formatter);
            LocalDateTime startOfDay = currentDate.atStartOfDay();
            LocalDateTime endOfDay = currentDate.atTime(23, 59, 59);
            long count = this.count(new LambdaQueryWrapper<EcoChainBuildWarehouse>()
                    .eq(EcoChainBuildWarehouse::getSocialCreditCode, getUserInfo.getSocialCreditCode())
                    .ge(EcoChainBuildWarehouse::getAddDatetime, startOfDay)
                    .le(EcoChainBuildWarehouse::getAddDatetime, endOfDay));
            count = count + 1l;
            String countString = String.valueOf(count);
            String batchNumber = "";
            if (count < 10l) {
                batchNumber = dateString + "-0" + countString;
            } else {
                batchNumber = dateString + "-" + countString;
            }
            param.setBatchNumber(batchNumber);

            this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }
    }

    @Override
    public Page<EcoChainBuildWarehouseReturnParam> selectPageBuildWarehouse(EcoChainBuildWareHousePageSearchParam param) {
        // 一、超级管理员用户登录，查询全部
        if ("-1".equals(getUserInfo.getUserType())) {
            LambdaQueryWrapper<EcoChainBuildWarehouse> queryWrapper = getQueryWrapper(param);

            List<EcoChainBuildWarehouse> list;
            long total;
            // 当processId存在时，查全部数据后在内存过滤再手动分页
            if (hasLatestProcessFilter(param)) {
                list = this.list(queryWrapper);
                total = 0; // 过滤后重新计算
            } else {
                Page<EcoChainBuildWarehouse> pageInfo = new Page<>(param.getCurrent(), param.getSize());
                this.page(pageInfo, queryWrapper);
                list = pageInfo.getRecords();
                total = pageInfo.getTotal();
            }
            if (list == null || list.isEmpty()) {
                return new Page<>();
            }

            List<EcoChainBuildWarehouseReturnParam> resultList = buildReturnParams(list, param, null);

            // 当processId存在时，手动分页
            if (hasLatestProcessFilter(param)) {
                return buildManualPage(resultList, param.getCurrent(), param.getSize());
            }

            Page<EcoChainBuildWarehouseReturnParam> pageResult = new Page<>(param.getCurrent(), param.getSize());
            pageResult.setTotal(total);
            pageResult.setRecords(resultList);

            return pageResult;
        }

        // 二、政府人员或二级管理员用户登录，查询管辖区域内数据
        if ("0".equals(getUserInfo.getUserType())) {
            LambdaQueryWrapper<EcoChainBuildWarehouse> queryWrapper = getQueryWrapper(param);

            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                // 获取当前用户的管辖区域列表
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    // 如果传入的areaId不合法，直接返回空的页面
                    if (!list.contains(param.getAreaId())) {
                        return new Page<>();
                    }
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        queryWrapper.eq(EcoChainBuildWarehouse::getAreaId, param.getAreaId());
                    }
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainBuildWarehouse::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainBuildWarehouse::getAreaId, list);
                }
            }

            List<EcoChainBuildWarehouse> list;
            long total;
            // 当 processId 或 processName 存在时，查全部数据后在内存过滤再手动分页
            if (hasLatestProcessFilter(param)) {
                list = this.list(queryWrapper);
                total = 0;
            } else {
                Page<EcoChainBuildWarehouse> pageInfo = new Page<>(param.getCurrent(), param.getSize());
                this.page(pageInfo, queryWrapper);
                list = pageInfo.getRecords();
                total = pageInfo.getTotal();
            }
            if (list == null || list.isEmpty()) {
                return new Page<>();
            }
            
            List<EcoChainBuildWarehouseReturnParam> resultList = buildReturnParams(list, param, null);

            // 当processId或processName存在时，手动分页
            if (hasLatestProcessFilter(param)) {
                return buildManualPage(resultList, param.getCurrent(), param.getSize());
            }

            Page<EcoChainBuildWarehouseReturnParam> pageResult = new Page<>(param.getCurrent(), param.getSize());
            pageResult.setTotal(total);
            pageResult.setRecords(resultList);

            return pageResult;
        }

        // 三、本企业人员登录
        if ("1".equals(getUserInfo.getUserType())) {
            LambdaQueryWrapper<EcoChainBuildWarehouse> queryWrapper = getQueryWrapper(param);

            queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainBuildWarehouse::getSocialCreditCode, getUserInfo.getSocialCreditCode());

            Page<EcoChainBuildWarehouse> pageInfo = new Page<>(param.getCurrent(), param.getSize());

            // 1.本企业管理员用户登录---查询当前企业的所有数据
            if ("1".equals(getUserInfo.getIsAdmin()) || UserUtils.get().getUserType() == 1  ) {
                List<EcoChainBuildWarehouse> list;
                long total;
                // 当 processId 或 processName 存在时，查全部数据后在内存过滤再手动分页
                if (hasLatestProcessFilter(param)) {
                    list = this.list(queryWrapper);
                    total = 0;
                } else {
                    this.page(pageInfo, queryWrapper);
                    list = pageInfo.getRecords();
                    total = pageInfo.getTotal();
                }
                if (list == null || list.isEmpty()) {
                    return new Page<>();
                }
                
                List<EcoChainBuildWarehouseReturnParam> resultList = buildReturnParams(list, param, null);

                // 当processId或processName存在时，手动分页
                if (hasLatestProcessFilter(param)) {
                    return buildManualPage(resultList, param.getCurrent(), param.getSize());
                }

                Page<EcoChainBuildWarehouseReturnParam> pageResult = new Page<>(param.getCurrent(), param.getSize());
                pageResult.setTotal(total);
                pageResult.setRecords(resultList);

                return pageResult;
            }

            // 2.本企业普通员工登录---查看被设为可见人的数据
            List<EcoChainBuildWarehouse> list = this.list(queryWrapper);
            if (list == null || list.isEmpty()) {
                return new Page<>();
            }

            // 2.1获取当前登录用户id，并根据用户id查询企业人员表id
            Long sysUserId = UserUtils.get().getId();
            SysUser one = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getId, sysUserId));
            if (one == null || one.getPeopleEnterpriseId() == null || one.getPeopleEnterpriseId() == 0L) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到当前登录用户的企业人员id");
            }
            Long id = one.getPeopleEnterpriseId();

            List<EcoChainBuildWarehouseReturnParam> resultList = buildReturnParams(list, param, id);

            // --手动分页
            return buildManualPage(resultList, param.getCurrent(), param.getSize());
        }
        return new Page<>();
    }

    private List<EcoChainBuildWarehouseReturnParam> buildReturnParams(List<EcoChainBuildWarehouse> list, EcoChainBuildWareHousePageSearchParam param, Long userId) {
        List<EcoChainBuildWarehouseReturnParam> resultList = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return resultList;
        }

        List<Long> warehouseIds = list.stream().map(EcoChainBuildWarehouse::getId).collect(Collectors.toList());

        // Batch query complete records to count pickings
        Map<Long, Long> pickingCountMap = new HashMap<>();
        // Batch query complete records to get latest completeData
        Map<Long, EcoChainCompleteRecord> latestCompleteRecordMap = new HashMap<>();
        // If completeRecord filter provided, record which warehouses match any complete record
        Set<Long> completeRecordMatchedWarehouseIdSet = new HashSet<>();
        // Batch query process trackings to get processData
        Map<Long, List<EcoChainProcessTracking>> processTrackingsMap = new HashMap<>();

        log.info("buildReturnParams 开始执行，总仓库数量: {}", warehouseIds.size());
        int batchSize = 500;
        for (int i = 0; i < warehouseIds.size(); i += batchSize) {
            List<Long> subList = warehouseIds.subList(i, Math.min(i + batchSize, warehouseIds.size()));

            List<EcoChainCompleteRecord> completeRecords = ecoChainCompleteRecordService.list(
                    new LambdaQueryWrapper<EcoChainCompleteRecord>().in(EcoChainCompleteRecord::getEcoChainBuildWarehouseId, subList)
            );
            if (completeRecords != null) {
                for (EcoChainCompleteRecord record : completeRecords) {
                    pickingCountMap.put(record.getEcoChainBuildWarehouseId(), pickingCountMap.getOrDefault(record.getEcoChainBuildWarehouseId(), 0L) + 1);

                    // keep latest complete record by recordingTime
                    if (record.getRecordingTime() != null) {
                        EcoChainCompleteRecord existing = latestCompleteRecordMap.get(record.getEcoChainBuildWarehouseId());
                        if (existing == null
                                || existing.getRecordingTime() == null
                                || record.getRecordingTime().isAfter(existing.getRecordingTime())) {
                            latestCompleteRecordMap.put(record.getEcoChainBuildWarehouseId(), record);
                        }
                    }

                    // fuzzy match on ANY complete record (not only latest)
                    if (StringUtils.isNotBlank(param.getCompleteRecord())
                            && StringUtils.isNotBlank(record.getCompleteRecord())
                            && record.getCompleteRecord().contains(param.getCompleteRecord())) {
                        completeRecordMatchedWarehouseIdSet.add(record.getEcoChainBuildWarehouseId());
                    }
                }
            }

            List<EcoChainProcessTracking> processTrackings = ecoChainProcessTrackingService.list(
                    new LambdaQueryWrapper<EcoChainProcessTracking>().in(EcoChainProcessTracking::getEcoChainBuildWarehouseId, subList)
            );
            if (processTrackings != null) {
                log.info("批次查询（条件IN），传入库ID数量: {}，数据库返回追踪记录总数: {}", subList.size(), processTrackings.size());
                for (EcoChainProcessTracking record : processTrackings) {
                    processTrackingsMap.computeIfAbsent(record.getEcoChainBuildWarehouseId(), k -> new ArrayList<>()).add(record);
                }
            }
        }

        for (EcoChainBuildWarehouse eachBuildWarehouse : list) {
            if (userId != null) {
                String visiblePeople = eachBuildWarehouse.getVisiblePeople() != null ? eachBuildWarehouse.getVisiblePeople() : "";
                String[] split = visiblePeople.split(",");
                if (!Arrays.asList(split).contains(String.valueOf(userId)) && !Objects.equals(UserUtils.get().getId(), eachBuildWarehouse.getCreatorId())) {
                    continue;
                }
            }

            EcoChainBuildWarehouseReturnParam returnParam = new EcoChainBuildWarehouseReturnParam();
            BeanUtils.copyProperties(eachBuildWarehouse, returnParam);

            long count = pickingCountMap.getOrDefault(eachBuildWarehouse.getId(), 0L);
            returnParam.setPicking(count > 0 ? count : 0L);

            // latest complete record (eco-chain-complete-record)
            EcoChainCompleteRecord completeData = latestCompleteRecordMap.get(eachBuildWarehouse.getId());
            // completeRecord filter (fuzzy match on ANY complete record)
            if (StringUtils.isNotBlank(param.getCompleteRecord())
                    && !completeRecordMatchedWarehouseIdSet.contains(eachBuildWarehouse.getId())) {
                continue;
            }
            returnParam.setCompleteData(completeData);

            Long cycle = 0L;
            if ("3".equals(eachBuildWarehouse.getStatus()) && eachBuildWarehouse.getAllCompletionDatetime() != null) {
                cycle = calculateCycle(eachBuildWarehouse);
            } else {
                if (eachBuildWarehouse.getAddDatetime() != null) {
                    cycle = Duration.between(eachBuildWarehouse.getAddDatetime(), LocalDateTime.now()).toDays();
                }
            }
            returnParam.setPeriod(cycle.intValue());

            // Handle processData filtering
            List<EcoChainProcessTracking> fullTrackingList = processTrackingsMap.getOrDefault(eachBuildWarehouse.getId(), new ArrayList<>());

            EcoChainProcessTracking processData = fullTrackingList.stream()
                    .filter(t -> t.getRecordingTime() != null)
                    .max(Comparator.comparing(EcoChainProcessTracking::getRecordingTime))
                    .orElse(null);
            boolean isProcessData = true;
            if (processData == null) {
                if (hasLatestProcessFilter(param)) {
                    // 有过滤条件但没有数据，跳过
                    continue;
                }
                // 没有过滤条件，可以添加该仓库
                returnParam.setProcessData(null);
                resultList.add(returnParam);
                continue;
            }


            if (param.getProcessId() != null) {
                isProcessData = Objects.equals(processData.getEcoChainProcessNodeConfigurationId(), param.getProcessId());
            }

            if (!isProcessData) {
                continue;
            }
            if (param.getProcessName() != null) {
                // 使用 contains 进行模糊匹配，同时避免空指针
                String processNameInData = processData.getProcessName();
                isProcessData = processNameInData != null && processNameInData.contains(param.getProcessName());
            }
            if (!isProcessData) {
                continue;
            }

            if (param.getLatestProcessStartTime() != null) {
                isProcessData = processData.getRecordingTime() != null
                        && !processData.getRecordingTime().isBefore(param.getLatestProcessStartTime());
            }
            if (!isProcessData) {
                continue;
            }

            if (param.getLatestProcessEndTime() != null) {
                isProcessData = processData.getRecordingTime() != null
                        && !processData.getRecordingTime().isAfter(param.getLatestProcessEndTime());
            }
            if (!isProcessData) {
                continue;
            }

            returnParam.setProcessData(processData);

            resultList.add(returnParam);
        }
        return resultList;
    }

    @Override
    public void removeBatchByIdList(List<Long> idList) {
        for (Long id : idList) {
            // 删除所有关联的EcoChainCompleteRecord
            List<EcoChainCompleteRecord> recordList = ecoChainCompleteRecordMapper.selectList(
                    new LambdaQueryWrapper<EcoChainCompleteRecord>()
                            .eq(EcoChainCompleteRecord::getEcoChainBuildWarehouseId, id));
            for (EcoChainCompleteRecord record : recordList) {
                if (ObjectUtils.isNotEmpty(record.getPictures())) {
                    List<Map<String, String>> list = FileManageUtil.parsePictureList(record.getPictures());
                    for (Map<String, String> map : list) {
                        String url = map.get("url");
                        if (!FileManageUtil.deleteFile(url))
                            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "图片删除失败");
                    }
                }
                ecoChainCompleteRecordMapper.deleteById(record.getId());
            }
            // 删除所有关联的EcoChainProcessTracking
            List<EcoChainProcessTracking> trackingList = ecoChainProcessTrackingMapper.selectList(
                    new LambdaQueryWrapper<EcoChainProcessTracking>()
                            .eq(EcoChainProcessTracking::getEcoChainBuildWarehouseId, id));
            for (EcoChainProcessTracking tracking : trackingList) {
                if (ObjectUtils.isNotEmpty(tracking.getProcessPictures())) {
                    List<Map<String, String>> list = FileManageUtil.parsePictureList(tracking.getProcessPictures());
                    for (Map<String, String> map : list) {
                        String url = map.get("url");
                        if (!FileManageUtil.deleteFile(url))
                            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "图片删除失败");
                    }
                }
                ecoChainProcessTrackingMapper.deleteById(tracking.getId());
            }
            // 删除EcoChainBuildWarehouse
            EcoChainBuildWarehouse warehouse = ecoChainBuildWarehouseMapper.selectById(id);
            if (ObjectUtils.isNotEmpty(warehouse.getWarahousePictures())) {
                List<Map<String, String>> list = FileManageUtil.parsePictureList(warehouse.getWarahousePictures());
                for (Map<String, String> map : list) {
                    String url = map.get("url");
                    if (!FileManageUtil.deleteFile(url))
                        throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "图片删除失败");
                }
            }
            ecoChainBuildWarehouseMapper.deleteById(id);
        }
    }

    @Override
    public Integer getPeriodByBuildWarehouseId(Long buildWarehouseId) {
        if (buildWarehouseId == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        Integer period = this.getPeriod(buildWarehouseId);
        return period;
    }

    @Override
    public boolean updateByWarehouseId(EcoChainBuildWarehouse param) {
        if (ObjectUtils.isEmpty(param) || param.getId() == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }

        EcoChainBuildWarehouse warehouse = this.getById(param.getId());
        if (warehouse == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "记录不存在");
        }

        if(ObjectUtils.isEmpty(param.getOperator())){
            if(ObjectUtils.isNotEmpty(UserUtils.get().getUsername())){
                param.setOperator(UserUtils.get().getUsername());
            }
        }
        if(ObjectUtils.isEmpty(param.getOperationDatetime())){
            LocalDateTime localDate = LocalDateTime.now();
            param.setOperationDatetime(localDate);
        }

        FileManageUtil.handlePictureUpdate(warehouse.getWarahousePictures(), param.getWarahousePictures());
        ecoChainBuildWarehouseMapper.updateByWarehouseId(param);
        return true;
    }

    @Override
    public EcoChainBuildWarehouseReturnParam selectBuildWarehouse(Long id) {


        // 获取生态链建库数据
        EcoChainBuildWarehouse ecoChainBuildWarehouse = ecoChainBuildWarehouseMapper.selectById(id);

        // 如果未找到库，返回 null 或抛出异常
        if (ecoChainBuildWarehouse == null) {
            return null;  // 或者可以抛出异常，比如 throw new BusinessException("未找到指定的库");
        }

        // 创建返回对象
        EcoChainBuildWarehouseReturnParam returnParam = new EcoChainBuildWarehouseReturnParam();
        // 将原始的 EcoChainBuildWarehouse 数据复制到返回对象
        BeanUtils.copyProperties(ecoChainBuildWarehouse, returnParam);
        // 获取周期，如果status为3并且allCompletionDatetime存在，调用calculateCycle方法，否则计算当前日期减去addDatetime
        Long cycle = 0L;
        if ("3".equals(ecoChainBuildWarehouse.getStatus()) && ecoChainBuildWarehouse.getAllCompletionDatetime() != null) {
            cycle = calculateCycle(ecoChainBuildWarehouse);  // 调用周期计算方法
        } else {
            // 如果状态不是3或allCompletionDatetime为空，使用当前日期减去addDatetime
            cycle = Duration.between(ecoChainBuildWarehouse.getAddDatetime(), LocalDateTime.now()).toDays();
        }

        returnParam.setPeriod(cycle.intValue());  // 转换为 Integer 类型


        // 设置采摘次数（picking）字段
        long pickingCount = countPicking(ecoChainBuildWarehouse.getId());
        returnParam.setPicking(pickingCount > 0 ? pickingCount : 0L);

        // 返回计算后的返回对象
        return returnParam;

    }
    // 统计采摘次数的方法
    private long countPicking(Long warehouseId) {
        return ecoChainCompleteRecordService.count(new LambdaQueryWrapper<EcoChainCompleteRecord>()
                .eq(EcoChainCompleteRecord::getEcoChainBuildWarehouseId, warehouseId));
    }

    /**
     * 库分页查询条件构造
     * @return
     */
    private LambdaQueryWrapper<EcoChainBuildWarehouse> getQueryWrapper(EcoChainBuildWareHousePageSearchParam param) {
        LambdaQueryWrapper<EcoChainBuildWarehouse> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(ObjectUtils.isNotEmpty(param.getIndustryWarehouse()),
                        EcoChainBuildWarehouse::getIndustryWarehouse, param.getIndustryWarehouse())
                .like(ObjectUtils.isNotEmpty(param.getIndustryGroupOption()), EcoChainBuildWarehouse::getIndustryGroupOption,
                        param.getIndustryGroupOption())
                .like(ObjectUtils.isNotEmpty(param.getVisiblePeopleName()), EcoChainBuildWarehouse::getVisiblePeopleName,
                        param.getVisiblePeopleName())
                .like(ObjectUtils.isNotEmpty(param.getBatchNumber()), EcoChainBuildWarehouse::getBatchNumber, param.getBatchNumber())
                .eq(ObjectUtils.isNotEmpty(param.getIsCase()), EcoChainBuildWarehouse::getIsCase, param.getIsCase())
                .ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainBuildWarehouse::getAddDatetime, param.getStartTime())
                .orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainBuildWarehouse::getOperationDatetime);

        if (ObjectUtils.isNotEmpty(param.getDetailTypeOption())) {
            queryWrapper.and(wrapper -> {
                for (int i = 0; i < param.getDetailTypeOption().size(); i++) {
                    String item = param.getDetailTypeOption().get(i);
                    if (i > 0) {
                        wrapper.or();
                    }
                    wrapper.like(EcoChainBuildWarehouse::getDetailTypeOption, item);
                }
            });
        }

        if (ObjectUtils.isNotEmpty(param.getEndTime())) {
            LocalDateTime endDate = param.getEndTime();
            endDate = endDate.plusDays(1);
            queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainBuildWarehouse::getAddDatetime, endDate);
        }

        if ("-1".equals(param.getStatus())) {
            // 查询param.getStatus()的值为0,1,2的情况
            queryWrapper.in(EcoChainBuildWarehouse::getStatus, Arrays.asList("0", "1", "2"));
        } else if ("-2".equals(param.getStatus())) {
            queryWrapper.in(EcoChainBuildWarehouse::getStatus, Arrays.asList("0", "1"));
        } else if (StringUtils.isNotBlank(param.getStatus())){
            queryWrapper.eq(ObjectUtils.isNotEmpty(param.getStatus()), EcoChainBuildWarehouse::getStatus, param.getStatus());
        }

        return queryWrapper;
    }

    private boolean hasLatestProcessFilter(EcoChainBuildWareHousePageSearchParam param) {
        return param.getProcessId() != null
                || param.getProcessName() != null
                || param.getLatestProcessStartTime() != null
                || param.getLatestProcessEndTime() != null
                || StringUtils.isNotBlank(param.getCompleteRecord());
    }





    private Integer getPeriod(Long buildWareHouseId) {

        // 查询得到当前库的完成记录
        List<EcoChainCompleteRecord> completeRecords = ecoChainCompleteRecordService.listByWarehouseId(buildWareHouseId);
        // 查询建库日期
        EcoChainBuildWarehouse buildWarehouse = this.getById(buildWareHouseId);
        if (buildWarehouse == null) {
            return 0;
        }
        LocalDateTime addDatetime = buildWarehouse.getAddDatetime();

        // 1.完成记录表无数据，周期 = 当前时间 - 建库时间
        if (completeRecords.isEmpty() && addDatetime != null) {
            long daysBetween = ChronoUnit.DAYS.between(addDatetime.toLocalDate(), LocalDate.now());
            Integer days = (int) daysBetween;
            return days;
        }
        // 2.完成记录表有数据，周期 = 完成记录表最小日期 - 建库日期
        if (!completeRecords.isEmpty() && addDatetime != null) {
            // 获取recordingTime最小的记录
            EcoChainCompleteRecord minRecord = completeRecords.stream()
                    .min(Comparator.comparing(EcoChainCompleteRecord::getRecordingTime))
                    .orElse(null);

            if (minRecord != null) {
                LocalDateTime minRecordingTime = minRecord.getRecordingTime();
                long days = ChronoUnit.DAYS.between(addDatetime.toLocalDate(), minRecordingTime.toLocalDate());
                return (int) days;
            }
        }

        return 0;
    }


    private long calculateCycle(EcoChainBuildWarehouse warehouse) {
        LocalDateTime now = LocalDateTime.now();
        long cycle = 0;

        // 判断状态和是否存在完成时间，来计算周期
        if ("3".equals(warehouse.getStatus()) && warehouse.getAllCompletionDatetime() != null) {
            // 如果状态是 3 且 allCompletionDatetime 存在，计算结束时间与开始时间的差
            cycle = Duration.between(warehouse.getAddDatetime(), warehouse.getAllCompletionDatetime()).toDays();
        } else {
            // 否则，使用当前时间减去开始时间
            cycle = Duration.between(warehouse.getAddDatetime(), now).toDays();
        }
        return cycle;
    }

    /**
     * 手动分页工具方法：将全部结果列表按当前页和每页大小进行切片
     * @param resultList 过滤后的全部结果
     * @param current 当前页码
     * @param size 每页大小
     * @return 分页结果
     */
    private Page<EcoChainBuildWarehouseReturnParam> buildManualPage(List<EcoChainBuildWarehouseReturnParam> resultList, long current, long size) {
        int fromIndex = (int) ((current - 1) * size);
        int toIndex = (int) Math.min(fromIndex + size, resultList.size());
        // 防止索引越界
        if (fromIndex > resultList.size()) {
            fromIndex = resultList.size();
        }
        if (toIndex > resultList.size()) {
            toIndex = resultList.size();
        }
        List<EcoChainBuildWarehouseReturnParam> currentPageRecords = resultList.subList(fromIndex, toIndex);
        Page<EcoChainBuildWarehouseReturnParam> pageResult = new Page<>(current, size);
        pageResult.setTotal(resultList.size());
        pageResult.setRecords(currentPageRecords);
        return pageResult;
    }

    /**
     * 根据库id查询过程追踪数据，并根据processId过滤，返回recordingTime最新的记录
     * @param warehouseId 库id
     * @param processId 过程节点配置id（可为空）
     * @return 最新的过程追踪记录，无数据时返回null
     */
    private EcoChainProcessTracking getProcessData(Long warehouseId, Long processId, String processName) {
        // 根据库id查询过程追踪列表
        List<EcoChainProcessTracking> trackingList = ecoChainProcessTrackingService.listByWarehouseId(warehouseId);

        // 如果没有返回值，将ProcessData设置为null
        if (trackingList == null || trackingList.isEmpty()) {
            return null;
        }

        // 如果processId存在，则根据ecoChainProcessNodeConfigurationId进行过滤
        if (processId != null) {
            trackingList = trackingList.stream()
                    .filter(t -> processId.equals(t.getEcoChainProcessNodeConfigurationId()))
                    .collect(Collectors.toList());
        }
        if (processName != null){
            trackingList = trackingList.stream()
                    .filter(t -> t.getProcessName() != null && t.getProcessName().contains(processName))
                    .collect(Collectors.toList());
        }

        // 挑选出recordingTime最新的记录
        return trackingList.stream()
                .filter(t -> t.getRecordingTime() != null)
                .max(Comparator.comparing(EcoChainProcessTracking::getRecordingTime))
                .orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer importBuildWarehouseData(MultipartFile file) throws IOException {
        // 1. 查询 eco_chain_type_configuration 表，构建类型映射
        List<EcoChainTypeConfiguration> typeConfigList = typeConfigurationService.list();
        Map<String, EcoChainTypeConfiguration> typeConfigMap = new HashMap<>();
        for (EcoChainTypeConfiguration config : typeConfigList) {
            typeConfigMap.put(config.getDetailType(), config);
        }

        // 2. 查询 eco_chain_regional_configuration 表，构建区域映射
        List<EcoChainRegionalConfiguration> regionalConfigList = regionalConfigurationService.list();
        Map<String, EcoChainRegionalConfiguration> regionalConfigMap = new HashMap<>();
        for (EcoChainRegionalConfiguration config : regionalConfigList) {
            regionalConfigMap.put(config.getIndustryGroup(), config);
        }

        // 3. 查询 people_enterprise 表，构建企业人员映射
        List<PeopleEnterprise> enterpriseList = peopleEnterpriseService.list();
        Map<String, PeopleEnterprise> enterpriseMap = new HashMap<>();
        for (PeopleEnterprise enterprise : enterpriseList) {
            enterpriseMap.put(enterprise.getPeopleName(), enterprise);
        }

        // 4. 获取当前登录用户的企业名称和社会信用代码
        String currentEnterpriseName = getUserInfo.getEnterpriseName();
        String currentSocialCreditCode = getUserInfo.getSocialCreditCode();

        if (currentEnterpriseName == null || currentSocialCreditCode == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未获取到当前登录用户的企业信息");
        }

        // 5. 执行导入
        ExcelReader excelReader = null;
        try {
            // 先创建 reader 获取 sheet 列表（现在只有一个 sheet）
            excelReader = EasyExcel.read(file.getInputStream()).build();
            List<ReadSheet> sheets = excelReader.excelExecutor().sheetList();

            int totalImportCount = 0;

            // 只处理第一个 sheet
            if (!sheets.isEmpty()) {
                ReadSheet sheet = sheets.get(0);
                log.info("开始导入 sheet: {}", sheet.getSheetName());

                // 创建 listener，不再需要传递 sheet 名称
                EcoChainBuildWarehouseImportListener listener = new EcoChainBuildWarehouseImportListener(
                        this, typeConfigMap, regionalConfigMap, enterpriseMap, currentEnterpriseName, currentSocialCreditCode);

                // 重新创建带 listener 的 reader 来读取当前 sheet
                // headRowNumber(1) 表示跳过第 1 行表头，从第 2 行开始读取数据
                ExcelReader sheetReader = EasyExcel.read(file.getInputStream(), 
                        EcoChainBuildWarehouseImportParam.class,
                        listener)
                        .headRowNumber(1)  // 跳过第 1 行表头
                        .build();

                ReadSheet readSheet = EasyExcel.readSheet(sheet.getSheetNo()).build();
                sheetReader.read(readSheet);

                // 累加导入数量
                totalImportCount += listener.getNumOfNew();

                log.info("sheet 导入完成，共导入 {} 条记录", listener.getNumOfNew());

                sheetReader.finish();
            }

            log.info("所有数据导入完成，总计导入 {} 条记录", totalImportCount);
            return totalImportCount;

        } finally {
            if (excelReader != null) {
                excelReader.finish();
            }
        }
    }

    @Override
    public boolean batchUpdateVisiblePeople(List<Long> warehouseIds, List<Long> visiblePeopleIds) {
        if (ObjectUtils.isEmpty(warehouseIds) || warehouseIds.isEmpty()) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "库 IDs 不能为空");
        }
        
        // 查询可见人信息，拼接姓名和 ID
        StringBuilder visiblePeopleIdsStr = new StringBuilder();
        StringBuilder visiblePeopleNamesStr = new StringBuilder();
        
        if (visiblePeopleIds != null && !visiblePeopleIds.isEmpty()) {
            for (Long peopleId : visiblePeopleIds) {
                PeopleEnterprise enterprise = peopleEnterpriseService.getById(peopleId);
                if (enterprise != null) {
                    if (visiblePeopleIdsStr.length() > 0) {
                        visiblePeopleIdsStr.append(",");
                        visiblePeopleNamesStr.append(",");
                    }
                    visiblePeopleIdsStr.append(peopleId);
                    visiblePeopleNamesStr.append(enterprise.getPeopleName());
                } else {
                    log.warn("未找到企业人员 ID: {}", peopleId);
                }
            }
        }
        
        // 批量更新
        for (Long warehouseId : warehouseIds) {
            EcoChainBuildWarehouse warehouse = this.getById(warehouseId);
            if (warehouse == null) {
                log.warn("未找到库 ID: {}", warehouseId);
                continue;
            }
            
            // 设置可见人信息
            warehouse.setVisiblePeople(visiblePeopleIdsStr.toString());
            warehouse.setVisiblePeopleName(visiblePeopleNamesStr.toString());
            
            // 设置操作人和操作时间
            if (ObjectUtils.isNotEmpty(UserUtils.get().getUsername())) {
                warehouse.setOperator(UserUtils.get().getUsername());
            }
            warehouse.setOperationDatetime(LocalDateTime.now());
            
            // 更新数据库
            this.updateById(warehouse);
        }
        
        return true;
    }
}

