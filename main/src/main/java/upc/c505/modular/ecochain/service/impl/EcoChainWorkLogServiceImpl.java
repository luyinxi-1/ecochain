package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import upc.c505.common.responseparam.R;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.entity.SysUser;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.auth.service.ISysUserService;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.*;
import upc.c505.modular.ecochain.entity.*;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.ExportWorkLogExcelParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.ExportWorkLogReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.SelectWorkLogReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.SelectWorkLogSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.EcoChainWorkStatisticsReturnParam;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.EcoChainWorkStatisticsSearchParam;
import upc.c505.modular.ecochain.mapper.*;
import upc.c505.modular.ecochain.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.service.IPeopleGovernmentService;
import upc.c505.modular.supenterprise.entity.SupEnterprise;
import upc.c505.modular.supenterprise.mapper.SupEnterpriseMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Collectors;
import upc.c505.modular.ecochain.config.PercentCellStyleHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author byh
 * @since 2025-04-16
 */
@Slf4j
@Service
public class EcoChainWorkLogServiceImpl extends ServiceImpl<EcoChainWorkLogMapper, EcoChainWorkLog> implements IEcoChainWorkLogService {

    private static final DateTimeFormatter LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private EcoChainProcessTrackingMapper ecoChainProcessTrackingMapper;

    @Autowired
    private EcoChainCompleteRecordMapper ecoChainCompleteRecordMapper;

    @Autowired
    private EcoChainWorkLogMapper ecoChainWorkLogMapper;
    @Autowired
    private IEcoChainBuildWarehouseService buildWarehouseService;
    @Autowired
    private IEcoChainProcessNodeConfigurationService processNodeConfigurationName;
    @Autowired
    private IEcoChainWorkLogService workLogService;
    @Autowired
    private EcoChainWorkLogMapper workLogMapper;
    @Autowired
    private SupEnterpriseMapper supEnterpriseMapper;
    @Autowired
    private EcoChainDictTypeMapper ecoChainDictTypeMapper;
    @Autowired
    private EcoChainDictDataMapper ecoChainDictDataMapper;
    @Autowired
    private GetUserInfo getUserInfo;
    @Autowired
    private IEcoChainBuildWarehouseService ecoChainBuildWarehouseService;
    @Autowired
    private IEcoChainCompleteRecordService ecoChainCompleteRecordService;
    @Autowired
    private IEcoChainProcessTrackingService ecoChainProcessTrackingService;
    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysAreaService sysAreaService;

    @Override
    public boolean updateWorkLog(EcoChainWorkLog param) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        param.setEvaluationDate(currentDateTime);
        if (ObjectUtils.isNotEmpty(param.getEcoChainCompleteRecordId())) {
            EcoChainWorkLog workLog = ecoChainWorkLogMapper.selectOne(new LambdaQueryWrapper<EcoChainWorkLog>().eq(EcoChainWorkLog::getEcoChainCompleteRecordId, param.getEcoChainCompleteRecordId()));
            if (ObjectUtils.isNotEmpty(workLog)) {
                param.setId(workLog.getId());
                return this.updateById(param);
            }
            param.setId(null);
            return this.save(param);
        } else if (ObjectUtils.isNotEmpty(param.getEcoChainProcessTrackingId())) {
            EcoChainWorkLog workLog = ecoChainWorkLogMapper.selectOne(new LambdaQueryWrapper<EcoChainWorkLog>().eq(EcoChainWorkLog::getEcoChainProcessTrackingId, param.getEcoChainProcessTrackingId()));
            if (ObjectUtils.isNotEmpty(workLog)) {
                param.setId(workLog.getId());
                return this.updateById(param);
            }
            param.setId(null);
            return this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "，追踪完成记录id和过程跟踪id不能同时为空");
        }
    }

    @Override
    public List<EcoChainWorkStatisticsReturnParam> workStatistics(EcoChainWorkStatisticsSearchParam param) {
        List<EcoChainWorkStatisticsReturnParam> list = new ArrayList<>();
        Map<String, EcoChainWorkStatisticsReturnParam> resultMap = new HashMap<>();
        // 获取当前登录用户的社会信用代码，筛选查询结果
        if (getUserInfo.getSocialCreditCode() == null || getUserInfo.getSocialCreditCode() == "") {
            throw new BusinessException(BusinessErrorEnum.NOT_PERMISSIONS, "，社会信用代码为空");
        }
        String socialCreditCode = getUserInfo.getSocialCreditCode();
        if (ObjectUtils.isEmpty(param.getName()))
            param.setName("");
        // 记录人姓名
        if (param.getSearchType() == 1) {
            List<Map<String, Object>> processTrackingNum = ecoChainProcessTrackingMapper.countByRecorder(param.getStartTime(), param.getEndTime(), param.getName(), socialCreditCode);
            List<Map<String, Object>> completeRecordNum = ecoChainCompleteRecordMapper.countByRecorder(param.getStartTime(), param.getEndTime(), param.getName(), socialCreditCode);
            // 处理processTrackingNum
            for (Map<String, Object> record : processTrackingNum) {
                if (ObjectUtils.isEmpty(record.get("recorder")) || ObjectUtils.isEmpty(record.get("count")))
                    continue;
                String recorder = (String) record.get("recorder");
                Long count = (Long) record.get("count");

                EcoChainWorkStatisticsReturnParam temp = new EcoChainWorkStatisticsReturnParam();
                temp.setName(recorder);
                temp.setProcessTrackingNum(count);
                temp.setCompleteRecordNum(0L);
                temp.setSumNum(count);

                resultMap.put(recorder, temp);
            }
            for (Map<String, Object> record : completeRecordNum) {
                if (ObjectUtils.isEmpty(record.get("recorder")) || ObjectUtils.isEmpty(record.get("count")))
                    continue;
                String recorder = (String) record.get("recorder");
                Long count = (Long) record.get("count");
                // 如果recorder已经存在，更新completeRecordNum字段，不存在则创建新对象
                if (resultMap.containsKey(recorder)) {
                    EcoChainWorkStatisticsReturnParam temp = resultMap.get(recorder);
                    temp.setCompleteRecordNum(count);
                    temp.setSumNum(count + temp.getProcessTrackingNum());
                    resultMap.put(recorder, temp);
                } else {
                    EcoChainWorkStatisticsReturnParam temp = new EcoChainWorkStatisticsReturnParam();
                    temp.setName(recorder);
                    temp.setProcessTrackingNum(0L);
                    temp.setCompleteRecordNum(count);
                    temp.setSumNum(count);
                    resultMap.put(recorder, temp);
                }
            }
            list = new ArrayList<>(resultMap.values());
        }
        // 类型
        else if (param.getSearchType() == 2) {
            List<Map<String, Object>> processTrackingNum = ecoChainProcessTrackingMapper.countByDetailTypeOption(param.getStartTime(), param.getEndTime(), param.getName(), socialCreditCode);
            List<Map<String, Object>> completeRecordNum = ecoChainCompleteRecordMapper.countByDetailTypeOption(param.getStartTime(), param.getEndTime(), param.getName(), socialCreditCode);

            for (Map<String, Object> record : processTrackingNum) {
                if (ObjectUtils.isEmpty(record.get("detailTypeOption")) || ObjectUtils.isEmpty(record.get("count")))
                    continue;
                String detailTypeOption = (String) record.get("detailTypeOption");
                Long count = (Long) record.get("count");

                EcoChainWorkStatisticsReturnParam temp = new EcoChainWorkStatisticsReturnParam();
                temp.setName(detailTypeOption);
                temp.setProcessTrackingNum(count);
                temp.setCompleteRecordNum(0L);
                temp.setSumNum(count);

                resultMap.put(detailTypeOption, temp);
            }
            for (Map<String, Object> record : completeRecordNum) {
                if (ObjectUtils.isEmpty(record.get("detailTypeOption")) || ObjectUtils.isEmpty(record.get("count")))
                    continue;
                String detailTypeOption = (String) record.get("detailTypeOption");
                Long count = (Long) record.get("count");

                if (resultMap.containsKey(detailTypeOption)) {
                    EcoChainWorkStatisticsReturnParam temp = resultMap.get(detailTypeOption);
                    temp.setCompleteRecordNum(count);
                    temp.setSumNum(count + temp.getProcessTrackingNum());
                    resultMap.put(detailTypeOption, temp);
                } else {
                    EcoChainWorkStatisticsReturnParam temp = new EcoChainWorkStatisticsReturnParam();
                    temp.setName(detailTypeOption);
                    temp.setProcessTrackingNum(0L);
                    temp.setCompleteRecordNum(count);
                    temp.setSumNum(count);
                    resultMap.put(detailTypeOption, temp);
                }
            }
            list = new ArrayList<>(resultMap.values());
        }
        // 区域
        else if (param.getSearchType() == 3) {
            List<Map<String, Object>> processTrackingNum = ecoChainProcessTrackingMapper.countByIndustryGroupOption(param.getStartTime(), param.getEndTime(), param.getName(), socialCreditCode);
            List<Map<String, Object>> completeRecordNum = ecoChainCompleteRecordMapper.countByIndustryGroupOption(param.getStartTime(), param.getEndTime(), param.getName(), socialCreditCode);

            for (Map<String, Object> record : processTrackingNum) {
                if (ObjectUtils.isEmpty(record.get("industryGroupOption")) || ObjectUtils.isEmpty(record.get("count")))
                    continue;
                String industryGroupOption = (String) record.get("industryGroupOption");
                Long count = (Long) record.get("count");

                EcoChainWorkStatisticsReturnParam temp = new EcoChainWorkStatisticsReturnParam();
                temp.setName(industryGroupOption);
                temp.setProcessTrackingNum(count);
                temp.setCompleteRecordNum(0L);
                temp.setSumNum(count);

                resultMap.put(industryGroupOption, temp);
            }
            for (Map<String, Object> record : completeRecordNum) {
                if (ObjectUtils.isEmpty(record.get("industryGroupOption")) || ObjectUtils.isEmpty(record.get("count")))
                    continue;
                String industryGroupOption = (String) record.get("industryGroupOption");
                Long count = (Long) record.get("count");

                if (resultMap.containsKey(industryGroupOption)) {
                    EcoChainWorkStatisticsReturnParam temp = resultMap.get(industryGroupOption);
                    temp.setCompleteRecordNum(count);
                    temp.setSumNum(count + temp.getProcessTrackingNum());
                    resultMap.put(industryGroupOption, temp);
                } else {
                    EcoChainWorkStatisticsReturnParam temp = new EcoChainWorkStatisticsReturnParam();
                    temp.setName(industryGroupOption);
                    temp.setProcessTrackingNum(0L);
                    temp.setCompleteRecordNum(count);
                    temp.setSumNum(count);
                    resultMap.put(industryGroupOption, temp);
                }
            }
            list = new ArrayList<>(resultMap.values());
        }
        // 项目名称
        else if (param.getSearchType() == 4) {
            List<Map<String, Object>> processTrackingNum = ecoChainProcessTrackingMapper.countByIndustryWarehouse(param.getStartTime(), param.getEndTime(), param.getName(), socialCreditCode);
            List<Map<String, Object>> completeRecordNum = ecoChainCompleteRecordMapper.countByIndustryWarehouse(param.getStartTime(), param.getEndTime(), param.getName(), socialCreditCode);

            for (Map<String, Object> record : processTrackingNum) {
                if (ObjectUtils.isEmpty(record.get("industryWarehouse")) || ObjectUtils.isEmpty(record.get("count")))
                    continue;
                String industryWarehouse = (String) record.get("industryWarehouse");
                Long count = (Long) record.get("count");

                EcoChainWorkStatisticsReturnParam temp = new EcoChainWorkStatisticsReturnParam();
                temp.setName(industryWarehouse);
                temp.setProcessTrackingNum(count);
                temp.setCompleteRecordNum(0L);
                temp.setSumNum(count);

                resultMap.put(industryWarehouse, temp);
            }
            for (Map<String, Object> record : completeRecordNum) {
                if (ObjectUtils.isEmpty(record.get("industryWarehouse")) || ObjectUtils.isEmpty(record.get("count")))
                    continue;
                String industryWarehouse = (String) record.get("industryWarehouse");
                Long count = (Long) record.get("count");

                if (resultMap.containsKey(industryWarehouse)) {
                    EcoChainWorkStatisticsReturnParam temp = resultMap.get(industryWarehouse);
                    temp.setCompleteRecordNum(count);
                    temp.setSumNum(count + temp.getProcessTrackingNum());
                    resultMap.put(industryWarehouse, temp);
                } else {
                    EcoChainWorkStatisticsReturnParam temp = new EcoChainWorkStatisticsReturnParam();
                    temp.setName(industryWarehouse);
                    temp.setProcessTrackingNum(0L);
                    temp.setCompleteRecordNum(count);
                    temp.setSumNum(count);
                    resultMap.put(industryWarehouse, temp);
                }
            }
            list = new ArrayList<>(resultMap.values());
        }
        // 按照sumNum降序
        list.sort((o1, o2) -> o2.getSumNum().compareTo(o1.getSumNum()));
        return list;
    }

    @Override
    public List<EcoChainWorkStatisticsSignContractRateReturnParam> workStatisticsSignContractRate(EcoChainWorkStatisticsSearchParam param) {
        List<EcoChainWorkStatisticsSignContractRateReturnParam> list = new ArrayList<>();
        Map<String, EcoChainWorkStatisticsSignContractRateReturnParam> resultMap = new HashMap<>();
        // 获取当前登录用户的社会信用代码，筛选查询结果
        if (getUserInfo.getSocialCreditCode() == null || getUserInfo.getSocialCreditCode() == "") {
            throw new BusinessException(BusinessErrorEnum.NOT_PERMISSIONS, "，社会信用代码为空");
        }
        if (ObjectUtils.isEmpty(param.getStartTime()) || ObjectUtils.isEmpty(param.getEndTime())) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "，时间参数不能为空");
        }
        String socialCreditCode = getUserInfo.getSocialCreditCode();
//        String socialCreditCode = "xinnong1"; // 测试用

        LambdaQueryWrapper<EcoChainProcessTracking> trackingLambdaQueryWrapper = new LambdaQueryWrapper<EcoChainProcessTracking>()
                .eq(EcoChainProcessTracking::getSocialCreditCode, socialCreditCode)
                .ge(EcoChainProcessTracking::getRecordingTime, param.getStartTime())
                .le(EcoChainProcessTracking::getRecordingTime, param.getEndTime());
        LambdaQueryWrapper<EcoChainCompleteRecord> completeLambdaQueryWrapper = new LambdaQueryWrapper<EcoChainCompleteRecord>()
                .eq(EcoChainCompleteRecord::getSocialCreditCode, socialCreditCode)
                .ge(EcoChainCompleteRecord::getRecordingTime, param.getStartTime())
                .le(EcoChainCompleteRecord::getRecordingTime, param.getEndTime());
        if (ObjectUtils.isNotEmpty(param.getName())) {
            trackingLambdaQueryWrapper.like(EcoChainProcessTracking::getRecorder, param.getName());
            completeLambdaQueryWrapper.like(EcoChainCompleteRecord::getRecorder, param.getName());
        }

        List<EcoChainProcessTracking> trackingList = ecoChainProcessTrackingMapper.selectList(trackingLambdaQueryWrapper);
        List<EcoChainCompleteRecord> completeList = ecoChainCompleteRecordMapper.selectList(completeLambdaQueryWrapper);

        Map<String, List<EcoChainProcessTracking>> trackGroupedByRecorder =
                Optional.ofNullable(trackingList)           // 防 NPE
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(item -> {
                            return ObjectUtils.isNotEmpty(item.getRecorder());
                        })
                        .collect(Collectors.groupingBy(
                                EcoChainProcessTracking::getRecorder, // 分组键
                                LinkedHashMap::new,
                                Collectors.toList()));

        Map<String, List<EcoChainCompleteRecord>> completeGroupedByRecorder =
                Optional.ofNullable(completeList)           // 防 NPE
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(item -> {
                            return ObjectUtils.isNotEmpty(item.getRecorder());
                        })
                        .collect(Collectors.groupingBy(
                                EcoChainCompleteRecord::getRecorder, // 分组键
                                LinkedHashMap::new,
                                Collectors.toList()));

        // 遍历集合，处理对应值
        for (Map.Entry<String, List<EcoChainProcessTracking>> entry : trackGroupedByRecorder.entrySet()) {
            Collection<EcoChainProcessTracking> uniqueByBuildWarehouseId = Optional
                    .ofNullable(entry.getValue())
                    .orElse(Collections.emptyList())
                    .stream()
                    // 转成 Map，key 冲突时保留第一个元素
                    .collect(Collectors.toMap(
                            EcoChainProcessTracking::getEcoChainBuildWarehouseId, // 分组键
                            Function.identity(),                // 值 = 本身
                            (oldVal, newVal) -> oldVal,         // 冲突解决：留旧扔新
                            LinkedHashMap::new))                // 保序
                    .values();                                  // Map ➝ Collection
            entry.setValue(new ArrayList<>(uniqueByBuildWarehouseId));
        }

        for (Map.Entry<String, List<EcoChainCompleteRecord>> entry : completeGroupedByRecorder.entrySet()) {
            Collection<EcoChainCompleteRecord> uniqueByBuildWarehouseId = Optional
                    .ofNullable(entry.getValue())
                    .orElse(Collections.emptyList())
                    .stream()
                    // 转成 Map，key 冲突时保留第一个元素
                    .collect(Collectors.toMap(
                            EcoChainCompleteRecord::getEcoChainBuildWarehouseId, // 分组键
                            Function.identity(),                // 值 = 本身
                            (oldVal, newVal) -> oldVal,         // 冲突解决：留旧扔新
                            LinkedHashMap::new))                // 保序
                    .values();                                  // Map ➝ Collection
            entry.setValue(new ArrayList<>(uniqueByBuildWarehouseId));
        }

        for (Map.Entry<String, List<EcoChainProcessTracking>> entry : trackGroupedByRecorder.entrySet()) {
            EcoChainWorkStatisticsSignContractRateReturnParam temp = new EcoChainWorkStatisticsSignContractRateReturnParam();
            temp.setName(entry.getKey());
            temp.setTrackingNum(entry.getValue().size());
            temp.setCompleteRecordNum(0);
            temp.setRate(0.0);
            resultMap.put(entry.getKey(), temp);
        }

        for (Map.Entry<String, List<EcoChainCompleteRecord>> entry : completeGroupedByRecorder.entrySet()) {
            // 如果recorder已经存在，更新completeRecordNum字段，不存在则创建新对象
            if (resultMap.containsKey(entry.getKey())) {
                EcoChainWorkStatisticsSignContractRateReturnParam temp = resultMap.get(entry.getKey());
                temp.setTrackingNum(temp.getTrackingNum() + entry.getValue().size());
                temp.setCompleteRecordNum(entry.getValue().size());
                if (temp.getCompleteRecordNum() != 0) {
                    temp.setRate((double) temp.getCompleteRecordNum() / temp.getTrackingNum());
                } else {
                    temp.setRate(0.0);
                }
                resultMap.put(entry.getKey(), temp);
            } else {
                EcoChainWorkStatisticsSignContractRateReturnParam temp = new EcoChainWorkStatisticsSignContractRateReturnParam();
                temp.setName(entry.getKey());
                temp.setTrackingNum(0);
                temp.setCompleteRecordNum(entry.getValue().size());
                temp.setRate(0.0);
                resultMap.put(entry.getKey(), temp);
            }
        }
        list = new ArrayList<>(resultMap.values());
        list.sort((o1, o2) -> o2.getRate().compareTo(o1.getRate()));
        return list;
    }

    @Override
    public EcoChainWorkLogGetByIdReturnParam getDetailsById(EcoChainWorkLogGetByIdParam param) {
        if (ObjectUtils.isEmpty(param.getType()) || ObjectUtils.isEmpty(param.getId())) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (param.getType() == 0) {
            return ecoChainWorkLogMapper.getDetailsByProcessId(param.getId());
        }
        return ecoChainWorkLogMapper.getDetailsByCompleteId(param.getId());
    }


    @Override
    public Page<SelectWorkLogReturnParam> selectWorkLog(SelectWorkLogSearchParam searchParam) {
        if (ObjectUtils.isEmpty(searchParam)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() == null || getUserInfo.getSocialCreditCode() == "") {
            throw new BusinessException(BusinessErrorEnum.NOT_PERMISSIONS, "社会信用代码为空");
        }
        searchParam.setSocialCreditCode(getUserInfo.getSocialCreditCode());
        List<SelectWorkLogReturnParam> resultList = new ArrayList<>();

        if ("1".equals(searchParam.getType())) {
            resultList = ecoChainWorkLogMapper.selectCompleteWorkLog(searchParam);
            resultList = resultList.stream()
                    .filter(item -> item.getId() != null)
                    .collect(Collectors.toList());

            resultList.stream().forEach(item -> item.setType("1"));
        } else if ("0".equals(searchParam.getType())) {
            resultList = ecoChainWorkLogMapper.selectProcessWorkLog(searchParam);
            resultList = resultList.stream()
                    .filter(item -> item.getId() != null)
                    .collect(Collectors.toList());
            resultList.stream().forEach(item -> item.setType("0"));
        } else {
            resultList = ecoChainWorkLogMapper.selectCompleteWorkLog(searchParam);
            resultList = resultList.stream()
                    .filter(item -> item.getId() != null)
                    .collect(Collectors.toList());
            resultList.stream().forEach(item -> item.setType("1"));
            List<SelectWorkLogReturnParam> resultListTemp = ecoChainWorkLogMapper.selectProcessWorkLog(searchParam);
            resultListTemp = resultListTemp.stream()
                    .filter(item -> item.getId() != null)
                    .collect(Collectors.toList());
            resultListTemp.stream().forEach(item -> item.setType("0"));
            resultList.addAll(resultListTemp);
        }

//        List<SupEnterprise> supEnterprises = supEnterpriseMapper.selectList(new MyLambdaQueryWrapper<SupEnterprise>().eq(SupEnterprise::getSocialCreditCode, getUserInfo.getSocialCreditCode()));
//        String industryCategory = supEnterprises.get(0).getIndustryCategory();
//        List<EcoChainDictType> ecoChainDictTypes = ecoChainDictTypeMapper.selectList(new MyLambdaQueryWrapper<EcoChainDictType>().eq(EcoChainDictType::getDictName, industryCategory));
//        String dictType = ecoChainDictTypes.get(0).getDictType();
//        List<EcoChainDictData> ecoChainDictData = ecoChainDictDataMapper.selectList(new MyLambdaQueryWrapper<EcoChainDictData>().eq(EcoChainDictData::getDictType, dictType).orderByAsc(EcoChainDictData::getId));
//
//        Map<String, String> mapList = new HashMap<>();
//        for (EcoChainDictData data : ecoChainDictData) {
//            if (data.getDictKey().equals("statusNotStarted")) {
//                mapList.put("0", data.getName());
//            }
//            if (data.getDictKey().equals("statusInProcess")) {
//                mapList.put("1", data.getName());
//            }
//            if (data.getDictKey().equals("statusInCompletion")) {
//                mapList.put("2", data.getName());
//            }
//            if (data.getDictKey().equals("statusCompletion")) {
//                mapList.put("3", data.getName());
//            }
//        }
//        resultList.forEach(item -> {
//            switch (item.getStatus()) {
//                case "0": item.setStatus("待跟踪"); break;
//                case "1": item.setStatus("意向客户"); break;
//                case "2": item.setStatus("成交客户"); break;
//                case "3": item.setStatus("完成"); break;
//            }
//            String status = item.getStatus();
//            String s = mapList.get(status);
//            item.setStatus(s);
//        });

        //添加对于warehouseeId的筛选



        // --此处需要手动设置分页
        // 计算当前页的起始索引
        int fromIndex = (int) ((searchParam.getCurrent() - 1) * searchParam.getSize());
        int toIndex = (int) Math.min(fromIndex + searchParam.getSize(), resultList.size());
        // 获取当前页的记录
        List<SelectWorkLogReturnParam> currentPageRecords = resultList.subList(fromIndex, toIndex);
        // 创建分页对象
        Page<SelectWorkLogReturnParam> pageResult = new Page<>(searchParam.getCurrent(), searchParam.getSize());
        pageResult.setTotal(resultList.size());
        pageResult.setRecords(currentPageRecords);

        return pageResult;
    }

    @Override
    public void exportWorkLog(String prefixUrl, HttpServletResponse response, SelectWorkLogSearchParam searchParam) {
        try {

            if (ObjectUtils.isEmpty(searchParam)) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
            }
            if (StringUtils.isNotBlank(getUserInfo.getSocialCreditCode())) {
                searchParam.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            }
            List<ExportWorkLogReturnParam> resultList = new ArrayList<>();
            if ("1".equals(searchParam.getType())) {
                resultList = workLogMapper.exportWorkLogCompleteRecord(searchParam);
                resultList = resultList.stream()
                        .filter(item -> item.getId() != null)
                        .collect(Collectors.toList());
            } else if ("0".equals(searchParam.getType())) {
                resultList = workLogMapper.exportWorkLogProcessTracking(searchParam);
                resultList = resultList.stream()
                        .filter(item -> item.getId() != null)
                        .collect(Collectors.toList());
            } else {
                resultList = workLogMapper.exportWorkLogCompleteRecord(searchParam);
                resultList = resultList.stream()
                        .filter(item -> item.getId() != null)
                        .collect(Collectors.toList());
                List<ExportWorkLogReturnParam> resultListTemp = workLogMapper.exportWorkLogProcessTracking(searchParam);
                resultListTemp = resultListTemp.stream()
                        .filter(item -> item.getId() != null)
                        .collect(Collectors.toList());
                resultList.addAll(resultListTemp);
            }

            List<SupEnterprise> supEnterprises = supEnterpriseMapper.selectList(new MyLambdaQueryWrapper<SupEnterprise>().eq(SupEnterprise::getSocialCreditCode, getUserInfo.getSocialCreditCode()));
            String industryCategory = supEnterprises.get(0).getIndustryCategory();
            List<EcoChainDictType> ecoChainDictTypes = ecoChainDictTypeMapper.selectList(new MyLambdaQueryWrapper<EcoChainDictType>().eq(EcoChainDictType::getDictName, industryCategory));
            String dictType = ecoChainDictTypes.get(0).getDictType();
            List<EcoChainDictData> ecoChainDictData = ecoChainDictDataMapper.selectList(new MyLambdaQueryWrapper<EcoChainDictData>().eq(EcoChainDictData::getDictType, dictType).orderByAsc(EcoChainDictData::getId));

            Map<String, String> mapList = new HashMap<>();
            for (EcoChainDictData data : ecoChainDictData) {
                if (data.getDictKey().equals("statusNotStarted")) {
                    mapList.put("0", data.getName());
                }
                if (data.getDictKey().equals("statusInProcess")) {
                    mapList.put("1", data.getName());
                }
                if (data.getDictKey().equals("statusInCompletion")) {
                    mapList.put("2", data.getName());
                }
                if (data.getDictKey().equals("statusCompletion")) {
                    mapList.put("3", data.getName());
                }
            }

            for (ExportWorkLogReturnParam item : resultList) {

//                switch (item.getStatus()) {
//                    case "0":
//                        item.setStatus("待跟踪");
//                        break;
//                    case "1":
//                        item.setStatus("意向客户");
//                        break;
//                    case "2":
//                        item.setStatus("成交客户");
//                        break;
//                    case "3":
//                        item.setStatus("完成");
//                        break;
//                }

                String status = item.getStatus();
                String s = mapList.get(status);
                item.setStatus(s);

                Long processNodeConfigurationId = item.getEcoChainProcessNodeConfigurationId();
                if (processNodeConfigurationId != null) {
                    EcoChainProcessNodeConfiguration processNodeConfiguration = processNodeConfigurationName.getById(processNodeConfigurationId);
                    if (processNodeConfiguration != null && processNodeConfiguration.getNodeName() != null) {
                        item.setEcoChainProcessNodeConfigurationName(processNodeConfiguration.getNodeName());
                    }
                }
            }


            if (resultList == null) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "查询结果为空");
            }

            List<ExportWorkLogExcelParam> excelList = new ArrayList<>();

            for (ExportWorkLogReturnParam item : resultList) {
                ExportWorkLogExcelParam excelItem = new ExportWorkLogExcelParam();
                BeanUtils.copyProperties(item, excelItem);

                String pictureJson = item.getPictures();

                try {
                    // 如果为空或格式不对，直接跳过该图片处理
                    if (pictureJson != null && pictureJson.trim().startsWith("[") && pictureJson.contains("url")) {
                        JSONArray jsonArray = JSONArray.parseArray(pictureJson);

                        // 多张图片或内容不是我们预期格式，就跳过（只处理 1 张图的情况）
                        if (jsonArray.size() == 1) {
                            JSONObject imageObj = jsonArray.getJSONObject(0);
                            String url = imageObj.getString("url");

                            // 判断是否是图片路径（不包含.mp4/.mov等视频后缀）
                            if (url != null && (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".webp"))) {
                                String fullUrl = prefixUrl + '/' + url;
                                byte[] imageBytes = downloadImage(fullUrl);
                                excelItem.setPicture(imageBytes);
                            }
                        }
                    }
                } catch (Exception e) {
                    // 打印异常信息但不中断循环
                    System.out.println("图片解析失败，跳过该记录：" + pictureJson);
                    e.printStackTrace();
                }

                excelList.add(excelItem);
            }

            // 设置响应头，确保浏览器或 Postman 识别为 Excel 下载
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=worklog.xlsx");

            // 写入数据到 Excel 表中
            EasyExcel.write(response.getOutputStream(), ExportWorkLogExcelParam.class)
                    .sheet("工作日志")
                    .doWrite(excelList);
            response.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException("导出工作日志失败", e);
        }
    }

    @Override
    public void exportWorkStatistics(HttpServletResponse response, EcoChainWorkStatisticsSearchParam param) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        if (ObjectUtils.isEmpty(param.getType()) || param.getType() == 0) {
            try {
                String fileName = URLEncoder.encode("工作统计列表", "UTF-8").replaceAll("\\+", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

                List<EcoChainWorkStatisticsReturnParam> ecoChainWorkStatisticsReturnParams = workStatistics(param);
                List<ExportEcoChainWorkStatisticsExcelParam> exportList = new ArrayList<>();
                int index = 1;
                for (EcoChainWorkStatisticsReturnParam interParam : ecoChainWorkStatisticsReturnParams) {
                    ExportEcoChainWorkStatisticsExcelParam endParam = new ExportEcoChainWorkStatisticsExcelParam();
                    endParam.setName(interParam.getName())
                            .setRanking(index)
                            .setSumNum(interParam.getSumNum().intValue())
                            .setProcessTrackingNum(interParam.getProcessTrackingNum().intValue())
                            .setCompleteRecordNum(interParam.getCompleteRecordNum().intValue());
                    index = index + 1;
                    exportList.add(endParam);
                }

                // 写入数据到 Excel 表中
                EasyExcel.write(response.getOutputStream(), ExportEcoChainWorkStatisticsExcelParam.class)
                        .sheet("工作统计列表")
                        .doWrite(exportList);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("导出失败，请重试");
            }
        } else {
            try {
                String fileName = URLEncoder.encode("工作统计列表-签单率", "UTF-8").replaceAll("\\+", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

                List<EcoChainWorkStatisticsSignContractRateReturnParam> ecoChainWorkStatisticsSignContractRateReturnParams = workStatisticsSignContractRate(param);
                List<ExportEcoChainWorkStatisticsSignContractRateExcelParam> exportList = new ArrayList<>();
                int index = 1;
                for (EcoChainWorkStatisticsSignContractRateReturnParam interParam : ecoChainWorkStatisticsSignContractRateReturnParams) {
                    ExportEcoChainWorkStatisticsSignContractRateExcelParam endParam = new ExportEcoChainWorkStatisticsSignContractRateExcelParam();
                    endParam.setName(interParam.getName())
                            .setRanking(index)
                            .setTrackingNum(interParam.getTrackingNum())
                            .setRate(interParam.getRate())
                            .setCompleteRecordNum(interParam.getCompleteRecordNum());
                    index = index + 1;
                    exportList.add(endParam);
                }

                // 写入数据到 Excel 表中
                EasyExcel.write(response.getOutputStream(), ExportEcoChainWorkStatisticsSignContractRateExcelParam.class)
                        .registerWriteHandler(new PercentCellStyleHandler())
                        .sheet("工作统计列表-签单率")
                        .doWrite(exportList);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("导出失败，请重试");
            }
        }
    }

    @Override
    public Page<EcoChainBuildWarehouseReturnParam> selectPageBuildWarehouseGroup(EcoChainBuildWareHousePageSearchParam param) {
        List<EcoChainBuildWarehouseReturnParam> resultList = listBuildWarehouseGroup(param, true);
        return buildManualPage(resultList, param.getCurrent(), param.getSize());
    }

    @Override
    public Page<EcoChainBuildWarehouseReturnParam> selectPageBuildWarehouseGroupByLogTime(EcoChainBuildWareHousePageSearchParam param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }

        List<SelectWorkLogReturnParam> workLogs = listAllWorkLogs(buildLogSearchParam(param));
        if (workLogs.isEmpty()) {
            return new Page<>();
        }

        Comparator<SelectWorkLogReturnParam> comparator = Comparator.comparing(
                SelectWorkLogReturnParam::getRecordingTime,
                Comparator.nullsLast(LocalDateTime::compareTo)
        );
        if (!Objects.equals(param.getIsAsc(), 1)) {
            comparator = comparator.reversed();
        }

        LinkedHashMap<Long, SelectWorkLogReturnParam> latestLogByWarehouseId = workLogs.stream()
                .filter(item -> item.getWarehouseId() != null)
                .sorted(comparator)
                .collect(Collectors.toMap(
                        SelectWorkLogReturnParam::getWarehouseId,
                        Function.identity(),
                        (first, second) -> first,
                        LinkedHashMap::new
                ));

        if (latestLogByWarehouseId.isEmpty()) {
            return new Page<>();
        }

        Map<Long, Integer> warehouseOrderMap = new HashMap<>();
        int order = 0;
        for (Long warehouseId : latestLogByWarehouseId.keySet()) {
            warehouseOrderMap.put(warehouseId, order++);
        }

        List<EcoChainBuildWarehouseReturnParam> resultList = listBuildWarehouseGroup(param, false).stream()
                .filter(item -> latestLogByWarehouseId.containsKey(item.getId()))
                .sorted(Comparator.comparingInt(item -> warehouseOrderMap.getOrDefault(item.getId(), Integer.MAX_VALUE)))
                .collect(Collectors.toList());

        return buildManualPage(resultList, param.getCurrent(), param.getSize());
    }

    private List<EcoChainBuildWarehouseReturnParam> listBuildWarehouseGroup(EcoChainBuildWareHousePageSearchParam param, boolean includeBuildTimeFilter) {
        // 一、超级管理员用户登录，查询全部
        if ("-1".equals(getUserInfo.getUserType())) {
            LambdaQueryWrapper<EcoChainBuildWarehouse> queryWrapper = getQueryWrapper(param, includeBuildTimeFilter);
            List<EcoChainBuildWarehouse> list = ecoChainBuildWarehouseService.list(queryWrapper);
            if (list == null || list.isEmpty()) {
                return new ArrayList<>();
            }
            return buildReturnParams(list, param, null);
        }

        // 二、政府人员或二级管理员用户登录，查询管辖区域内数据
        if ("0".equals(getUserInfo.getUserType())) {
            LambdaQueryWrapper<EcoChainBuildWarehouse> queryWrapper = getQueryWrapper(param, includeBuildTimeFilter);

            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    if (!list.contains(param.getAreaId())) {
                        return new ArrayList<>();
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

            List<EcoChainBuildWarehouse> list = ecoChainBuildWarehouseService.list(queryWrapper);
            if (list == null || list.isEmpty()) {
                return new ArrayList<>();
            }
            return buildReturnParams(list, param, null);
        }

        // 三、本企业人员登录
        if ("1".equals(getUserInfo.getUserType())) {
            LambdaQueryWrapper<EcoChainBuildWarehouse> queryWrapper = getQueryWrapper(param, includeBuildTimeFilter);
            queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainBuildWarehouse::getSocialCreditCode, getUserInfo.getSocialCreditCode());

            List<EcoChainBuildWarehouse> list = ecoChainBuildWarehouseService.list(queryWrapper);
            if (list == null || list.isEmpty()) {
                return new ArrayList<>();
            }

            if ("1".equals(getUserInfo.getIsAdmin()) || UserUtils.get().getUserType() == 1) {
                return buildReturnParams(list, param, null);
            }

            Long sysUserId = UserUtils.get().getId();
            SysUser one = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getId, sysUserId));
            if (one == null || one.getPeopleEnterpriseId() == null || one.getPeopleEnterpriseId() == 0L) {
                throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到当前登录用户的企业人员id");
            }
            return buildReturnParams(list, param, one.getPeopleEnterpriseId());
        }
        return new ArrayList<>();
    }

    private List<EcoChainBuildWarehouseReturnParam> buildReturnParams(List<EcoChainBuildWarehouse> list, EcoChainBuildWareHousePageSearchParam param, Long userId) {
        List<EcoChainBuildWarehouseReturnParam> resultList = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return resultList;
        }

        List<Long> warehouseIds = list.stream().map(EcoChainBuildWarehouse::getId).collect(Collectors.toList());
        
        // 分批查询，防止 SQL IN() 超过 1000 限制
        Map<Long, List<EcoChainCompleteRecord>> completeRecordsMap = new HashMap<>();
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
                    completeRecordsMap.computeIfAbsent(record.getEcoChainBuildWarehouseId(), k -> new ArrayList<>()).add(record);
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

            List<EcoChainCompleteRecord> completeRecords = completeRecordsMap.getOrDefault(eachBuildWarehouse.getId(), new ArrayList<>());
            List<EcoChainProcessTracking> fullTrackingList = processTrackingsMap.getOrDefault(eachBuildWarehouse.getId(), new ArrayList<>());

            long count = completeRecords.size();
            returnParam.setPicking(count > 0 ? count : 0L);

            Long cycle = 0L;
            if ("3".equals(eachBuildWarehouse.getStatus()) && eachBuildWarehouse.getAllCompletionDatetime() != null) {
                cycle = calculateCycle(eachBuildWarehouse);
            } else {
                if (eachBuildWarehouse.getAddDatetime() != null) {
                    cycle = java.time.Duration.between(eachBuildWarehouse.getAddDatetime(), LocalDateTime.now()).toDays();
                }
            }
            returnParam.setPeriod(cycle.intValue());

            EcoChainProcessTracking processData = fullTrackingList.stream()
                    .filter(t -> t.getRecordingTime() != null)
                    .max(Comparator.comparing(EcoChainProcessTracking::getRecordingTime))
                    .orElse(null);
            boolean isProcessData = true;
            if (processData == null) {
                if (param.getProcessId() != null || param.getProcessName() != null) {
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
                String processNameInData = processData.getProcessName();
                isProcessData = processNameInData != null && processNameInData.contains(param.getProcessName());
            }
            if (!isProcessData) {
                continue;
            }

            returnParam.setProcessData(processData);

            resultList.add(returnParam);
        }
        return resultList;
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
    private LambdaQueryWrapper<EcoChainBuildWarehouse> getQueryWrapper(EcoChainBuildWareHousePageSearchParam param, boolean includeBuildTimeFilter) {
        LambdaQueryWrapper<EcoChainBuildWarehouse> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(ObjectUtils.isNotEmpty(param.getIndustryWarehouse()),
                        EcoChainBuildWarehouse::getIndustryWarehouse, param.getIndustryWarehouse())
                .like(ObjectUtils.isNotEmpty(param.getIndustryGroupOption()), EcoChainBuildWarehouse::getIndustryGroupOption,
                        param.getIndustryGroupOption())
                .like(ObjectUtils.isNotEmpty(param.getVisiblePeopleName()), EcoChainBuildWarehouse::getVisiblePeopleName,
                        param.getVisiblePeopleName())
                .like(ObjectUtils.isNotEmpty(param.getBatchNumber()), EcoChainBuildWarehouse::getBatchNumber, param.getBatchNumber())
                .eq(ObjectUtils.isNotEmpty(param.getIsCase()), EcoChainBuildWarehouse::getIsCase, param.getIsCase())
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

        if (includeBuildTimeFilter) {
            queryWrapper.ge(ObjectUtils.isNotEmpty(param.getStartTime()), EcoChainBuildWarehouse::getAddDatetime, param.getStartTime());
            if (ObjectUtils.isNotEmpty(param.getEndTime())) {
                LocalDateTime endDate = param.getEndTime();
                endDate = endDate.plusDays(1);
                queryWrapper.le(ObjectUtils.isNotEmpty(param.getEndTime()), EcoChainBuildWarehouse::getAddDatetime, endDate);
            }
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

    private SelectWorkLogSearchParam buildLogSearchParam(EcoChainBuildWareHousePageSearchParam param) {
        SelectWorkLogSearchParam searchParam = new SelectWorkLogSearchParam();
        searchParam.setCurrent(1L);
        searchParam.setSize(Long.MAX_VALUE);
        searchParam.setType(null);
        searchParam.setDetailTypeOption(param.getDetailTypeOption());
        searchParam.setIndustryGroupOption(param.getIndustryGroupOption());
        searchParam.setIndustryWarehouse(param.getIndustryWarehouse());
        searchParam.setStatus(param.getStatus());
        if (ObjectUtils.isNotEmpty(param.getStartTime())) {
            searchParam.setRecordingTimeStart(param.getStartTime().format(LOG_TIME_FORMATTER));
        }
        if (ObjectUtils.isNotEmpty(param.getEndTime())) {
            searchParam.setRecordingTimeEnd(param.getEndTime().format(LOG_TIME_FORMATTER));
        }
        return searchParam;
    }

    private List<SelectWorkLogReturnParam> listAllWorkLogs(SelectWorkLogSearchParam searchParam) {
        if (ObjectUtils.isEmpty(searchParam)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (StringUtils.isBlank(getUserInfo.getSocialCreditCode())) {
            throw new BusinessException(BusinessErrorEnum.NOT_PERMISSIONS, "社会信用代码为空");
        }

        searchParam.setSocialCreditCode(getUserInfo.getSocialCreditCode());
        List<SelectWorkLogReturnParam> resultList = new ArrayList<>();

        if ("1".equals(searchParam.getType())) {
            resultList = ecoChainWorkLogMapper.selectCompleteWorkLog(searchParam);
            resultList = resultList.stream()
                    .filter(item -> item.getId() != null)
                    .peek(item -> item.setType("1"))
                    .collect(Collectors.toList());
        } else if ("0".equals(searchParam.getType())) {
            resultList = ecoChainWorkLogMapper.selectProcessWorkLog(searchParam);
            resultList = resultList.stream()
                    .filter(item -> item.getId() != null)
                    .peek(item -> item.setType("0"))
                    .collect(Collectors.toList());
        } else {
            List<SelectWorkLogReturnParam> completeLogs = ecoChainWorkLogMapper.selectCompleteWorkLog(searchParam).stream()
                    .filter(item -> item.getId() != null)
                    .peek(item -> item.setType("1"))
                    .collect(Collectors.toList());
            List<SelectWorkLogReturnParam> processLogs = ecoChainWorkLogMapper.selectProcessWorkLog(searchParam).stream()
                    .filter(item -> item.getId() != null)
                    .peek(item -> item.setType("0"))
                    .collect(Collectors.toList());
            resultList.addAll(completeLogs);
            resultList.addAll(processLogs);
        }

        return resultList;
    }
    private EcoChainProcessTracking getProcessDataFromList(List<EcoChainProcessTracking> trackingList, Long processId) {
        // 如果没有返回值，将ProcessData设置为null
        if (trackingList == null || trackingList.isEmpty()) {
            return null;
        }

        // 如果processId存在，则根据ecoChainProcessNodeConfigurationId进行过滤
        if (processId != null) {
            trackingList = trackingList.stream()
                    .filter(t -> processId.equals(t.getEcoChainProcessNodeConfigurationId()))
                    .collect(java.util.stream.Collectors.toList());
            // 过滤后如果没有匹配的数据，返回null
            if (trackingList.isEmpty()) {
                return null;
            }
        }

        // 挑选出recordingTime最新的记录
        return trackingList.stream()
                .filter(t -> t.getRecordingTime() != null)
                .max(Comparator.comparing(EcoChainProcessTracking::getRecordingTime))
                .orElse(null);
    }
    private byte[] downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        }
    }

}
