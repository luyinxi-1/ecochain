package upc.c505.modular.ecochain.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.controller.param.EcoChainWorkLogParam.ExportEcoChainAiSimulationReverseAssessmentExcelParam;
import upc.c505.modular.ecochain.entity.EcoChainAiSimulationReverseAssessment;
import upc.c505.modular.ecochain.entity.EcoChainCompleteRecord;
import upc.c505.modular.ecochain.entity.EcoChainExtendedTable;
import upc.c505.modular.ecochain.entity.EcoChainProcessTracking;
import upc.c505.modular.ecochain.mapper.EcoChainAiSimulationReverseAssessmentMapper;
import upc.c505.modular.ecochain.mapper.EcoChainCompleteRecordMapper;
import upc.c505.modular.ecochain.mapper.EcoChainExtendedTableMapper;
import upc.c505.modular.ecochain.mapper.EcoChainProcessTrackingMapper;
import upc.c505.modular.ecochain.service.IEcoChainAiSimulationReverseAssessmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.entity.PeopleEnterprise;
import upc.c505.modular.people.mapper.PeopleEnterpriseMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author la
 * @since 2025-04-16
 */
@Service
public class EcoChainAiSimulationReverseAssessmentServiceImpl extends ServiceImpl<EcoChainAiSimulationReverseAssessmentMapper, EcoChainAiSimulationReverseAssessment> implements IEcoChainAiSimulationReverseAssessmentService {

    @Autowired
    private EcoChainAiSimulationReverseAssessmentMapper ecoChainAiSimulationReverseAssessmentMapper;

    @Autowired
    private EcoChainCompleteRecordMapper ecoChainCompleteRecordMapper;

    @Autowired
    private EcoChainProcessTrackingMapper ecoChainProcessTrackingMapper;

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private PeopleEnterpriseMapper peopleEnterpriseMapper;

    @Autowired
    private EcoChainExtendedTableMapper ecoChainExtendedTableMapper;

    @Override
    public List<EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam> getNumerByTime(EcoChainAiSimulationReverseAssessmentGetNumberByTimeParam param) {
        if (ObjectUtils.isEmpty(param.getYear())) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        List<EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam> resultList = new ArrayList<>();
        List<EcoChainCompleteRecord> completeRecords = new ArrayList<>();
        List<EcoChainProcessTracking> processTrackings = new ArrayList<>();
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        if (ObjectUtils.isNotEmpty(param.getMonth())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startDateTime = LocalDateTime.parse(param.getYear() + "-" + param.getMonth() + "-01T00:00:00");
            endDateTime = startDateTime.plusMonths(1).minusNanos(1);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startDateTime = LocalDateTime.parse(param.getYear() + "-" + "01" + "-" + "01T00:00:00");
            endDateTime = LocalDateTime.parse(param.getYear() + "-12-31T23:59:59");
        }

        String socialCreditCode = getUserInfo.getSocialCreditCode();

        completeRecords = ecoChainCompleteRecordMapper.selectList(new MyLambdaQueryWrapper<EcoChainCompleteRecord>()
                .eq(ObjectUtils.isNotEmpty(param.getPeopleName()), EcoChainCompleteRecord::getRecorder, param.getPeopleName())
                .ge(EcoChainCompleteRecord::getRecordingTime, startDateTime)
                .le(EcoChainCompleteRecord::getRecordingTime, endDateTime)
                .eq(ObjectUtils.isNotEmpty(socialCreditCode), EcoChainCompleteRecord::getSocialCreditCode, socialCreditCode)
        );

        processTrackings = ecoChainProcessTrackingMapper.selectList(new MyLambdaQueryWrapper<EcoChainProcessTracking>()
                .eq(ObjectUtils.isNotEmpty(param.getPeopleName()), EcoChainProcessTracking::getRecorder, param.getPeopleName())
                .ge(EcoChainProcessTracking::getRecordingTime, startDateTime)
                .le(EcoChainProcessTracking::getRecordingTime, endDateTime)
                .eq(ObjectUtils.isNotEmpty(socialCreditCode), EcoChainProcessTracking::getSocialCreditCode, socialCreditCode)
        );

        Map<String, EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam> timeMap = new HashMap<>();
        if (ObjectUtils.isEmpty(param.getMonth())) {
            for (int i = 1; i <= 12; i++) {
                String timeKey = param.getYear() + "-" + String.format("%02d", i);
                EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam returnParam = new EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam();
                returnParam.setTime(timeKey);
                timeMap.put(timeKey, returnParam);
            }
        } else {
            int daysInMonth = YearMonth.of(Integer.parseInt(param.getYear()), Integer.parseInt(param.getMonth())).lengthOfMonth();
            for (int i = 1; i <= daysInMonth; i++) {
                String timeKey = param.getYear() + "-" + param.getMonth() + "-" + String.format("%02d", i);
                EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam returnParam = new EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam();
                returnParam.setTime(timeKey);
                timeMap.put(timeKey, returnParam);
            }
        }

        // 统计完成记录
        for (EcoChainCompleteRecord record : completeRecords) {
            String timeKey = ObjectUtils.isNotEmpty(param.getMonth()) ? record.getRecordingTime().toLocalDate().toString() : record.getRecordingTime().getYear() + "-" + String.format("%02d", record.getRecordingTime().getMonthValue());
            EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam returnParam = timeMap.getOrDefault(timeKey, new EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam());
            returnParam.setTime(timeKey);
            returnParam.setCompleteRecordNumber(returnParam.getCompleteRecordNumber() + 1);
            timeMap.put(timeKey, returnParam);
        }

        for (EcoChainProcessTracking tracking : processTrackings) {
            String timeKey = ObjectUtils.isNotEmpty(param.getMonth()) ? tracking.getRecordingTime().toLocalDate().toString() : tracking.getRecordingTime().getYear() + "-" + String.format("%02d", tracking.getRecordingTime().getMonthValue());
            EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam returnParam = timeMap.getOrDefault(timeKey, new EcoChainAiSimulationReverseAssessmentGetNumberByTimeReturnParam());
            returnParam.setTime(timeKey);
            returnParam.setProcessTrackingNumber(returnParam.getProcessTrackingNumber() + 1);
            timeMap.put(timeKey, returnParam);
        }

        resultList.addAll(timeMap.values());
        resultList.sort((o1, o2) -> {
            // 按照时间字符串进行排序
            if (o1.getTime() != null && o2.getTime() != null) {
                return o1.getTime().compareTo(o2.getTime());
            }
            return 0; // 如果时间为空不排序
        });


        return resultList;
    }

    @Override
    public List<EcoChainAiSimulationReverseAssessmentReturnParam> aiSimulationReverseAssessment(EcoChainAiSimulationReverseAssessmentSearchParam param) {
        // 非要在这个函数里也完成getPeopleBasicSalaryAndScore的功能
        // 获取人员的社会信用代码
        String socialCreditCode = getUserInfo.getSocialCreditCode();
//        String socialCreditCode = "2025423";

        // 查出本企业所有人员列表
        List<PeopleEnterprise> company = peopleEnterpriseMapper.selectList(
                new LambdaQueryWrapper<PeopleEnterprise>()
                        .eq(PeopleEnterprise::getSocialCreditCode,socialCreditCode)
        );

        // 查出EcoChainAiSimulationReverseAssessment表中的该企业的人员数据
        LambdaQueryWrapper<EcoChainAiSimulationReverseAssessment> queryWrapper0 = new LambdaQueryWrapper<>();
        queryWrapper0.eq(EcoChainAiSimulationReverseAssessment::getSocialCreditCode, socialCreditCode);
        List<EcoChainAiSimulationReverseAssessment> peopleList = ecoChainAiSimulationReverseAssessmentMapper.selectList(queryWrapper0);

        // 找出缺失的姓名并补记录
        Set<String> existingNames = peopleList.stream()
                .map(EcoChainAiSimulationReverseAssessment::getName)
                .collect(Collectors.toSet());
        for (PeopleEnterprise pe : company) {
            String name = pe.getPeopleName();
            if (!existingNames.contains(name)) {
                // 构造新记录
                EcoChainAiSimulationReverseAssessment rec = new EcoChainAiSimulationReverseAssessment();
                rec.setName(name);
                rec.setPeopleEnterpriseId(pe.getId());
                rec.setSocialCreditCode(socialCreditCode);

                ecoChainAiSimulationReverseAssessmentMapper.insert(rec);
            }
        }



        // 入参中没有传姓名的时候，要查整个公司的所有返回值；传的话就查这个人的
        if(ObjectUtils.isNotEmpty(param.getName()) || ObjectUtils.isNotNull(param.getName())){
            List<EcoChainAiSimulationReverseAssessmentReturnParam> resultList = new ArrayList<>();
            EcoChainAiSimulationReverseAssessmentReturnParam result = new EcoChainAiSimulationReverseAssessmentReturnParam();

            // 该企业在PeopleEnterprise表中没有员工
            if(ObjectUtils.isEmpty(company) || ObjectUtils.isNull(company)){
                resultList.add(result);
                return resultList;
            }

            PeopleEnterprise people = peopleEnterpriseMapper.selectOne(
                    new LambdaQueryWrapper<PeopleEnterprise>()
                            .eq(PeopleEnterprise::getSocialCreditCode,socialCreditCode)
                            .eq(PeopleEnterprise::getPeopleName,param.getName())
            );
            if (ObjectUtils.isNull(people) || ObjectUtils.isEmpty(people)){
//                throw new IllegalStateException("未找到该社会信用代码对应的人员信息");
                return resultList;
            }
            if (people.getCheckStatus() != null && people.getCheckStatus() == 0) {
//                throw new IllegalStateException("该人员已不在此公司");
                return resultList;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;
            try {
                startDateTime = LocalDateTime.parse(param.getStartTime(), formatter);
                endDateTime = LocalDateTime.parse(param.getEndTime(), formatter);
            } catch (Exception e) {
                // 处理时间解析错误
                throw new IllegalArgumentException("时间格式不正确，正确格式为 yyyy-MM-dd HH:mm:ss", e);
            } // 将字符串转换为 LocalDateTime

            LambdaQueryWrapper<PeopleEnterprise> queryWrapper5 = new LambdaQueryWrapper<>();
            queryWrapper5.eq(PeopleEnterprise::getSocialCreditCode, socialCreditCode)
                    .eq(PeopleEnterprise::getCheckStatus,1);
            List<PeopleEnterprise> peList = peopleEnterpriseMapper.selectList(queryWrapper5);

            //根据人名获取过程跟踪数量
            LambdaQueryWrapper<EcoChainProcessTracking> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.between(EcoChainProcessTracking::getRecordingTime, startDateTime, endDateTime);
            List<EcoChainProcessTracking> list1 = ecoChainProcessTrackingMapper.selectList(queryWrapper1);
            long processTrackingNumber = list1.stream() // 将 List 转换为 Stream
                    .filter(record ->
                            Objects.equals(record.getRecorder(), param.getName()) &&
                                    Objects.equals(record.getSocialCreditCode(), socialCreditCode)
                    ) // 筛选出 recorder 字段与目标姓名和社会信用代码相等的记录 (使用 Objects.equals 进行 null 安全比较)
                    .count(); // 计算筛选后满足条件的记录数量
            result.setProcessTrackingNumber(processTrackingNumber);

            //根据人名获取完成记录数量
            LambdaQueryWrapper<EcoChainCompleteRecord> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.between(EcoChainCompleteRecord::getRecordingTime, startDateTime, endDateTime);
            List<EcoChainCompleteRecord> list2 = ecoChainCompleteRecordMapper.selectList(queryWrapper2);
            long completedRecordsNumber = list2.stream() // 将 List 转换为 Stream
                    .filter(record ->
                            Objects.equals(record.getRecorder(), param.getName()) &&
                                    Objects.equals(record.getSocialCreditCode(), socialCreditCode)
                    ) // 筛选出 recorder 字段与目标姓名和社会信用代码相等相等的记录 (使用 Objects.equals 进行 null 安全比较)
                    .count(); // 计算筛选后满足条件的记录数量
            result.setCompletedRecordsNumber(completedRecordsNumber);

            //根据社会信用代码获取整个公司的工作记录加分，签单成交加分。函数中直接拿去用
            LambdaQueryWrapper<EcoChainExtendedTable> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(EcoChainExtendedTable::getSocialCreditCode, socialCreditCode);
            Long workRecordMarks = ecoChainExtendedTableMapper.selectOne(queryWrapper3).getWorkRecordMarks();
            Long signContractMarks = ecoChainExtendedTableMapper.selectOne(queryWrapper3).getSignContractMarks();

            long workScore = processTrackingNumber * workRecordMarks;
            result.setWorkScore(workScore);

            long performanceScore = completedRecordsNumber * signContractMarks;
            result.setPerformanceScore(performanceScore);

            //获取基础分、基础工资、总计得分、k值（都是根据人名得出的）
            LambdaQueryWrapper<EcoChainAiSimulationReverseAssessment> queryWrapper4 = new LambdaQueryWrapper<>();
            List<EcoChainAiSimulationReverseAssessment> list3 = ecoChainAiSimulationReverseAssessmentMapper.selectList(queryWrapper4);
            Optional<EcoChainAiSimulationReverseAssessment> matchingAssessmentOpt = list3.stream() // 将 List 转换为 Stream
                    .filter(assessment ->
                            Objects.equals(assessment.getName(), param.getName()) &&
                                    Objects.equals(assessment.getSocialCreditCode(), socialCreditCode)
                    ) // 筛选出 name 字段与目标姓名相等的记录 (使用 Objects.equals 进行 null 安全比较)
                    .findFirst(); // 找到第一个匹配的记录（如果存在）
            EcoChainAiSimulationReverseAssessment foundAssessment = matchingAssessmentOpt.get();
            Long basicScore = foundAssessment.getBasicScore();
            Long basicSalary = foundAssessment.getBasicSalary();
            if(basicSalary == null || basicScore == null){

                long totalScore = workScore + performanceScore;
                result.setTotalScore(totalScore);
                if(param.getSortFlag()==1){
                    result.setRank(peList.size());
                }else{
                    result.setRank(1);
                }
                result.setName(param.getName());
            }else{
                result.setBasicScore(basicScore);

                long totalScore = workScore + performanceScore + basicScore;
                result.setTotalScore(totalScore);

                double k = (double) basicSalary / totalScore;
                result.setKValue(k);

                //根据k值进行排名
                // 提取姓名列表
                List<String> peopleNames = peList.stream()
                        .map(PeopleEnterprise::getPeopleName)
                        .collect(Collectors.toList());

                List<Map.Entry<String, Double>> kList = new ArrayList<>();
                for (String name : peopleNames) {
                    EcoChainAiSimulationReverseAssessmentExceptRankReturnParam ecoChainAiSimulationReverseAssessmentExceptRankReturnParam = aiSimulationReverseAssessmentExceptRank(
                            name, list1, list2, list3, workRecordMarks, signContractMarks, socialCreditCode
                    );
                    kList.add(new AbstractMap.SimpleEntry<>(name, ecoChainAiSimulationReverseAssessmentExceptRankReturnParam.getKValue()));
                }

                if (param.getSortFlag() != null && param.getSortFlag() == 1) {
                    // 从大到小
                    kList.sort(Comparator.comparing(Map.Entry<String, Double>::getValue).reversed());
                } else {
                    // 默认（sortFlag=0）从小到大
                    kList.sort(Comparator.comparing(Map.Entry<String, Double>::getValue));
                }

                //找出 param.getName() 的排名（从1开始计数）
                int rank = 0;
                for (int i = 0; i < kList.size(); i++) {
                    if (Objects.equals(kList.get(i).getKey(), param.getName())) {
                        rank = i + 1;
                        break;
                    }
                }
                result.setRank(rank);
                result.setName(param.getName());
            }
            resultList.add(result);
            return resultList;
        }else {
            List<EcoChainAiSimulationReverseAssessmentReturnParam> resultList = new ArrayList<>();

            // 该企业在PeopleEnterprise表中没有员工
            if(ObjectUtils.isEmpty(company) || ObjectUtils.isNull(company)){
                return resultList;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;
            try {
                startDateTime = LocalDateTime.parse(param.getStartTime(), formatter);
                endDateTime = LocalDateTime.parse(param.getEndTime(), formatter);
            } catch (Exception e) {
                // 处理时间解析错误
                throw new IllegalArgumentException("时间格式不正确，正确格式为 yyyy-MM-dd HH:mm:ss", e);
            } // 将字符串转换为 LocalDateTime

            LambdaQueryWrapper<PeopleEnterprise> queryWrapper5 = new LambdaQueryWrapper<>();
            queryWrapper5.eq(PeopleEnterprise::getSocialCreditCode, socialCreditCode)
                    .eq(PeopleEnterprise::getCheckStatus,1);
            List<PeopleEnterprise> peList = peopleEnterpriseMapper.selectList(queryWrapper5);
            if (peList == null || peList.isEmpty()) {
                return resultList;
            }


            //获取指定时间范围内的过程跟踪记录
            LambdaQueryWrapper<EcoChainProcessTracking> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.between(EcoChainProcessTracking::getRecordingTime, startDateTime, endDateTime)
                    .eq(EcoChainProcessTracking::getSocialCreditCode, socialCreditCode);
            List<EcoChainProcessTracking> list1 = ecoChainProcessTrackingMapper.selectList(queryWrapper1);


            //获取指定时间范围内的完成记录
            LambdaQueryWrapper<EcoChainCompleteRecord> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.between(EcoChainCompleteRecord::getRecordingTime, startDateTime, endDateTime)
                    .eq(EcoChainCompleteRecord::getSocialCreditCode, socialCreditCode);
            List<EcoChainCompleteRecord> list2 = ecoChainCompleteRecordMapper.selectList(queryWrapper2);


            //根据社会信用代码获取整个公司的工作记录加分，签单成交加分。函数中直接拿去用
            LambdaQueryWrapper<EcoChainExtendedTable> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(EcoChainExtendedTable::getSocialCreditCode, socialCreditCode);
            EcoChainExtendedTable extendedTable = ecoChainExtendedTableMapper.selectOne(queryWrapper3);
            Long workRecordMarks = 0L; // 默认值
            Long signContractMarks = 0L; // 默认值
            if (extendedTable != null) {
                workRecordMarks = extendedTable.getWorkRecordMarks() != null ? extendedTable.getWorkRecordMarks() : 0L;
                signContractMarks = extendedTable.getSignContractMarks() != null ? extendedTable.getSignContractMarks() : 0L;
            }

            //获取基础分、基础工资、总计得分、k值（都是根据人名得出的）
            LambdaQueryWrapper<EcoChainAiSimulationReverseAssessment> queryWrapper4 = new LambdaQueryWrapper<>();
            queryWrapper4.eq(EcoChainAiSimulationReverseAssessment::getSocialCreditCode,socialCreditCode);
            List<EcoChainAiSimulationReverseAssessment> list3 = ecoChainAiSimulationReverseAssessmentMapper.selectList(queryWrapper4);

            //遍历员工，调用函数计算指标
            List<EcoChainAiSimulationReverseAssessmentExceptRankReturnParam> calculatedResults = new ArrayList<>();
            for (PeopleEnterprise person : peList) {
                String currentName = person.getPeopleName();
                if (currentName == null) continue; // 跳过名字为空的员工
                // 调用辅助函数计算该员工的指标
                EcoChainAiSimulationReverseAssessmentExceptRankReturnParam personResult = aiSimulationReverseAssessmentExceptRank(
                        currentName, list1, list2, list3, workRecordMarks, signContractMarks, socialCreditCode
                );
                calculatedResults.add(personResult);
            }

            if (param.getSortFlag() != null && param.getSortFlag() == 1) {
                // K 值从大到小排序
                calculatedResults.sort(Comparator.comparingDouble(EcoChainAiSimulationReverseAssessmentExceptRankReturnParam::getKValue).reversed());
            } else {
                // K 值从小到大排序 (默认)
                calculatedResults.sort(Comparator.comparingDouble(EcoChainAiSimulationReverseAssessmentExceptRankReturnParam::getKValue));
            }

            // 生成最终结果列表，并处理 K 值为 null 的情况
            for (int i = 0; i < calculatedResults.size(); i++) {
                EcoChainAiSimulationReverseAssessmentExceptRankReturnParam calculatedPerson = calculatedResults.get(i);
                EcoChainAiSimulationReverseAssessmentReturnParam finalResultItem = new EcoChainAiSimulationReverseAssessmentReturnParam();
                finalResultItem.setName(calculatedPerson.getName());
                finalResultItem.setProcessTrackingNumber(calculatedPerson.getProcessTrackingNumber());
                finalResultItem.setCompletedRecordsNumber(calculatedPerson.getCompletedRecordsNumber());
                finalResultItem.setWorkScore(calculatedPerson.getWorkScore());
                finalResultItem.setPerformanceScore(calculatedPerson.getPerformanceScore());
                finalResultItem.setBasicScore(calculatedPerson.getBasicScore()); // basicScore 可能为 null
                finalResultItem.setTotalScore(calculatedPerson.getTotalScore());
                finalResultItem.setRank(i + 1);

                if (calculatedPerson.getBasicScore() == null  ) {
                    finalResultItem.setKValue(null); // 设置为 null (要求 finalResultItem.kValue 是 Double 类型)
                } else {
                    // 否则，使用计算出的 K 值
                    finalResultItem.setKValue(calculatedPerson.getKValue());
                }
                resultList.add(finalResultItem);
            }
            return resultList;
        }
    }

    public EcoChainAiSimulationReverseAssessmentExceptRankReturnParam aiSimulationReverseAssessmentExceptRank(String name,List<EcoChainProcessTracking> list1,List<EcoChainCompleteRecord> list2,List<EcoChainAiSimulationReverseAssessment> list3,Long workRecordMarks,Long signContractMarks,String socialCreditCode) {
        EcoChainAiSimulationReverseAssessmentExceptRankReturnParam returnParam = new EcoChainAiSimulationReverseAssessmentExceptRankReturnParam();
        returnParam.setName(name);
        //根据人名获取过程跟踪数量
        long processTrackingNumber = list1.stream() // 将 List 转换为 Stream
                .filter(record ->
                        Objects.equals(record.getRecorder(), name) &&
                                Objects.equals(record.getSocialCreditCode(), socialCreditCode)
                ) // 筛选出 recorder 字段与目标姓名相等的记录 (使用 Objects.equals 进行 null 安全比较)
                .count(); // 计算筛选后满足条件的记录数量
        returnParam.setProcessTrackingNumber(processTrackingNumber);

        //根据人名获取完成记录数量
        long completedRecordsNumber = list2.stream() // 将 List 转换为 Stream
                .filter(record ->
                        Objects.equals(record.getRecorder(), name) &&
                                Objects.equals(record.getSocialCreditCode(), socialCreditCode)
                ) // 筛选出 recorder 字段与目标姓名相等的记录 (使用 Objects.equals 进行 null 安全比较)
                .count(); // 计算筛选后满足条件的记录数量
        returnParam.setCompletedRecordsNumber(completedRecordsNumber);

        long workScore = processTrackingNumber * workRecordMarks;
        returnParam.setWorkScore(workScore);
        long performanceScore = completedRecordsNumber * signContractMarks;
        returnParam.setPerformanceScore(performanceScore);

        //获取基础分、基础工资、总计得分、k值（都是根据人名得出的）
        Optional<EcoChainAiSimulationReverseAssessment> matchingAssessmentOpt = list3.stream() // 将 List 转换为 Stream
                .filter(assessment -> Objects.equals(assessment.getName(), name)) // 筛选出 name 字段与目标姓名相等的记录 (使用 Objects.equals 进行 null 安全比较)
                .findFirst(); // 找到第一个匹配的记录（如果存在）
        EcoChainAiSimulationReverseAssessment foundAssessment = matchingAssessmentOpt.get();
        Long basicScore = foundAssessment.getBasicScore();
        Long basicSalary = foundAssessment.getBasicSalary();
        returnParam.setBasicScore(basicScore);

        if(basicScore == null || basicSalary == null){
            returnParam.setKValue(0.0);
            long totalScore = workScore + performanceScore;
            returnParam.setTotalScore(totalScore);
        }else{
            long totalScore = workScore + performanceScore + basicScore;
            returnParam.setTotalScore(totalScore);
            double k = (double) basicSalary / totalScore;
            returnParam.setKValue(k);
        }
        return returnParam;
    }


    @Override
    public List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam> getPeopleBasicSalaryAndScore() {
        String socialCreditCode = getUserInfo.getSocialCreditCode();
        //String socialCreditCode = "2025418";

        // 查出本企业所有人员列表
        List<PeopleEnterprise> companyPeopleList = peopleEnterpriseMapper.selectList(
                new LambdaQueryWrapper<PeopleEnterprise>()
                        .eq(PeopleEnterprise::getSocialCreditCode,socialCreditCode)
        );

        // 查出EcoChainAiSimulationReverseAssessment表中的该企业的人员数据
        LambdaQueryWrapper<EcoChainAiSimulationReverseAssessment> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(EcoChainAiSimulationReverseAssessment::getSocialCreditCode, socialCreditCode);
        List<EcoChainAiSimulationReverseAssessment> peopleList = ecoChainAiSimulationReverseAssessmentMapper.selectList(queryWrapper2);

        // 找出缺失的姓名并补记录
        Set<String> existingNames = peopleList.stream()
                .map(EcoChainAiSimulationReverseAssessment::getName)
                .collect(Collectors.toSet());

        for (PeopleEnterprise pe : companyPeopleList) {
            String name = pe.getPeopleName();
            if (!existingNames.contains(name)) {
                // 构造新记录
                EcoChainAiSimulationReverseAssessment rec = new EcoChainAiSimulationReverseAssessment();
                rec.setName(name);
                rec.setPeopleEnterpriseId(pe.getId());
                rec.setSocialCreditCode(socialCreditCode);

                ecoChainAiSimulationReverseAssessmentMapper.insert(rec);

                peopleList.add(rec);
            }
        }

        List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam> returnList = new ArrayList<>(peopleList.size());
        for (EcoChainAiSimulationReverseAssessment entity : peopleList) {
            EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam dto = new EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam();
            dto.setId(entity.getId());
            dto.setName(entity.getName());               // 人员姓名
            dto.setBasicSalary(entity.getBasicSalary()); // 基础工资
            dto.setBasicScore(entity.getBasicScore());   // 基础分
            returnList.add(dto);
        }
        return returnList;
    }


    @Override
    public void exportAiSimulationReverseAssessment(HttpServletResponse response, EcoChainAiSimulationReverseAssessmentSearchParam param) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        try {
            // 设置文件名并进行URL编码
            String fileName = URLEncoder.encode("AI模拟反向测评列表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 获取查询数据
            List<EcoChainAiSimulationReverseAssessmentReturnParam> resultList = aiSimulationReverseAssessment(param);
            if (resultList == null || resultList.isEmpty()) {
                throw new RuntimeException("没有找到数据，无法导出");
            }

            // 创建导出数据的列表
            List<ExportEcoChainAiSimulationReverseAssessmentExcelParam> exportList = new ArrayList<>();

            // 格式化数据，准备导出
            for (EcoChainAiSimulationReverseAssessmentReturnParam result : resultList) {
                ExportEcoChainAiSimulationReverseAssessmentExcelParam exportItem = new ExportEcoChainAiSimulationReverseAssessmentExcelParam();
                exportItem.setName(result.getName() != null ? result.getName() : "未知")
                        .setRank(result.getRank() != null ? result.getRank().intValue() : 0)
                        .setProcessTrackingNumber(result.getProcessTrackingNumber() != null ? result.getProcessTrackingNumber().intValue() : 0)
                        .setCompletedRecordsNumber(result.getCompletedRecordsNumber() != null ? result.getCompletedRecordsNumber().intValue() : 0)
                        .setWorkScore(result.getWorkScore() != null ? result.getWorkScore().intValue() : 0)
                        .setPerformanceScore(result.getPerformanceScore() != null ? result.getPerformanceScore().intValue() : 0)
                        .setBasicScore(result.getBasicScore() != null ? result.getBasicScore().intValue() : 0)
                        .setTotalScore(result.getTotalScore() != null ? result.getTotalScore().intValue() : 0)
                        .setKValue(result.getKValue() != null ? result.getKValue() : 0)
                        .setRank(result.getRank() != null ? result.getRank().intValue() : 0);
                exportList.add(exportItem);
            }

            // 使用 EasyExcel 将数据写入 Excel 文件
            EasyExcel.write(response.getOutputStream(), ExportEcoChainAiSimulationReverseAssessmentExcelParam.class)
                    .sheet("AI模拟反向测评")
                    .doWrite(exportList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败，请重试");
        } catch (Exception e) {
            // 捕获其他异常并返回更详细的错误信息
            e.printStackTrace();
            throw new RuntimeException("导出过程中出现异常: " + e.getMessage());
        }
    }


//    @Override
//    public List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam> updateBasicInfoWithResult(EcoChainAiSimulationReverseAssessmentUpdateParam param) {
//        // 测试专用
//        //String socialCreditCode = "2025418";  // 硬编码测试信用代码
//        String socialCreditCode = getUserInfo.getSocialCreditCode(); // 正式环境恢复这行
//
//        // 1. 执行更新操作
//        LambdaUpdateWrapper<EcoChainAiSimulationReverseAssessment> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper
//                .eq(EcoChainAiSimulationReverseAssessment::getSocialCreditCode, socialCreditCode)
//                .eq(EcoChainAiSimulationReverseAssessment::getPeopleEnterpriseId, param.getId())
//                .set(EcoChainAiSimulationReverseAssessment::getBasicScore, param.getBasicScore())
//                .set(EcoChainAiSimulationReverseAssessment::getBasicSalary, param.getBasicSalary());
//
//        int updateCount = baseMapper.update(null, updateWrapper);
//
//        // 2. 如果更新失败（找不到对应记录）
//        if(updateCount == 0){
//            throw new RuntimeException("未找到对应人员记录或没有修改权限");
//        }
//
//        // 3. 返回最新数据（复用已有查询方法）
//        return getPeopleBasicSalaryAndScore();
//    }

    @Override
    public List<EcoChainAiSimulationReverseAssessmentGetSalaryAndScoreReturnParam> updateBasicInfoWithResult(List<EcoChainAiSimulationReverseAssessmentUpdateParam> params) {
        // 测试专用
        //String socialCreditCode = "2025418";  // 硬编码测试信用代码
        String socialCreditCode = getUserInfo.getSocialCreditCode(); // 正式环境恢复这行

        // 1. 执行更新操作
        params.forEach(param -> {
            LambdaUpdateWrapper<EcoChainAiSimulationReverseAssessment> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper
                    .eq(EcoChainAiSimulationReverseAssessment::getSocialCreditCode, socialCreditCode)
                    .eq(EcoChainAiSimulationReverseAssessment::getId, param.getId())
                    .set(EcoChainAiSimulationReverseAssessment::getBasicScore, param.getBasicScore())
                    .set(EcoChainAiSimulationReverseAssessment::getBasicSalary, param.getBasicSalary());

            int updateCount = baseMapper.update(null, updateWrapper);
            if (updateCount == 0) {
                throw new RuntimeException("未找到对应人员记录或没有修改权限");
            }
        });
        // 3. 返回最新数据
        return getPeopleBasicSalaryAndScore();
    }



}
