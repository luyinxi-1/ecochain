package upc.c505.modular.ecochain.service.impl;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainDataStatistics;
import upc.c505.modular.ecochain.entity.EcoChainProductClassification;
import upc.c505.modular.ecochain.entity.EcoChainProductManager;
import upc.c505.modular.ecochain.mapper.EcoChainDataStatisticsMapper;
import upc.c505.modular.ecochain.service.IEcoChainDataStatisticsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.service.IEcoChainProductClassificationService;
import upc.c505.modular.ecochain.service.IEcoChainProductManagerService;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.people.entity.PeopleEnterprise;
import upc.c505.modular.people.mapper.PeopleEnterpriseMapper;
import upc.c505.modular.people.service.IPeopleEnterpriseService;
import upc.c505.modular.people.service.IPeopleGovernmentService;
import java.util.List;
import upc.c505.modular.ecochain.controller.param.EcoChainDataStatisticsNameValuePairParam;
import upc.c505.modular.supenterprise.entity.SupEnterprise;
import upc.c505.modular.supenterprise.mapper.SupEnterpriseMapper;
import upc.c505.modular.supenterprise.service.ISupEnterpriseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author la
 * @since 2024-09-26
 */
@Service
public class EcoChainDataStatisticsServiceImpl extends ServiceImpl<EcoChainDataStatisticsMapper, EcoChainDataStatistics> implements IEcoChainDataStatisticsService {

    public static final String ECO_CHAIN_SCAN = "生态链扫码访问";
    public static final String COMPANY_SCAN = "企业扫码访问";
    public static final String PRODUCT_SCAN = "产品扫码";
    public static final String SHARE = "分享";
    public static final String BRAND_STORY = "品牌故事";
    public static final String BASIC_INFO = "基本信息";
    public static final String HONORS_QUALIFICATIONS = "荣誉资质";
    public static final String VIDEO_SURVEILLANCE = "视频监控";
    public static final String INDEX = "首页";
    public static final String PRODUCT_CENTER = "产品中心";
    public static final String CONTACT_BOSS = "联系老板";
    public static final String MERCHANT_PROMISE = "商家承诺";
    public static final String BUSINESS_CARD_SHARE_BROWSE = "名片分享浏览";
    public static final String PRODUCT_SHARE_BROWSE = "产品分享浏览";
    public static final String COMPANY_HOMEPAGE_SHARE_BROWSE = "公司主页分享浏览";
    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private EcoChainDataStatisticsMapper ecoChainDataStatisticsMapper;

    @Autowired
    private IEcoChainProductManagerService ecoChainProductManagerService;

    @Autowired
    private IEcoChainProductClassificationService ecoChainProductClassificationService;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private PeopleEnterpriseMapper peopleEnterpriseMapper;

    @Autowired
    private SupEnterpriseMapper supEnterpriseMapper;

    @Override
    public Boolean insertDataStatistics(EcoChainDataStatistics ecoChainDataStatistics) {
        if (ObjectUtils.isEmpty(ecoChainDataStatistics)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }

        if (ObjectUtils.isEmpty(ecoChainDataStatistics.getAreaId())) {
            if (ObjectUtils.isNotEmpty(ecoChainDataStatistics.getSocialCreditCode())) {
                List<SupEnterprise> supEnterprises = supEnterpriseMapper.selectList(new LambdaQueryWrapper<SupEnterprise>().eq(SupEnterprise::getSocialCreditCode, ecoChainDataStatistics.getSocialCreditCode()));
                List<PeopleEnterprise> list = peopleEnterpriseMapper.selectList(new LambdaQueryWrapper<PeopleEnterprise>().eq(PeopleEnterprise::getSocialCreditCode, ecoChainDataStatistics.getSocialCreditCode()));
                if (ObjectUtils.isNotEmpty(supEnterprises)) {
                    SupEnterprise supEnterprise = supEnterprises.get(0);
                    if (ObjectUtils.isNotEmpty(supEnterprise.getAreaId())) {
                        ecoChainDataStatistics.setAreaId(supEnterprise.getAreaId());
                    }
                }
                if (ObjectUtils.isEmpty(ecoChainDataStatistics.getAreaId()) && ObjectUtils.isNotEmpty(list)) {
                    PeopleEnterprise peopleEnterprise = list.get(0);
                    if (ObjectUtils.isNotEmpty(peopleEnterprise.getEnterpriseAreaId())) {
                        ecoChainDataStatistics.setAreaId(peopleEnterprise.getEnterpriseAreaId());
                    }
                }
            }

            if (ObjectUtils.isNotEmpty(getUserInfo.getAreaId()) && ObjectUtils.isEmpty(ecoChainDataStatistics.getAreaId())) {
                ecoChainDataStatistics.setAreaId(getUserInfo.getAreaId());
            }

        }

        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {

            if (ObjectUtils.isEmpty(ecoChainDataStatistics.getSocialCreditCode())) {
                ecoChainDataStatistics.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            }

            if (ObjectUtils.isEmpty(ecoChainDataStatistics.getEnterpriseName())) {
                ecoChainDataStatistics.setEnterpriseName(getUserInfo.getEnterpriseName());
            }

        }


        if (ecoChainDataStatistics.getProductName() == null) {
            EcoChainProductManager productManager =
                    ecoChainProductManagerService.getById(ecoChainDataStatistics.getEcoChainProductManagerId());
            if (productManager != null) {
                ecoChainDataStatistics.setProductName(productManager.getProductName());
            }
        }

        if (ecoChainDataStatistics.getProductClassification() == null) {
            EcoChainProductClassification productClassification =
                    ecoChainProductClassificationService.getById(ecoChainDataStatistics.getEcoChainProductClassificationId());
            if (productClassification != null) {
                ecoChainDataStatistics.setProductClassification(productClassification.getProductClassificationName());
            }

        }
        return this.save(ecoChainDataStatistics);

    }

    @Override
    public EcoChainAnalyticalOverviewReturnParamOne selectAnalyticalOverview(EcoChainAnalyticalOverviewSearchParam param) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = LocalDateTime.parse(param.getStartTime(), formatter);
            endDateTime = LocalDateTime.parse(param.getEndTime(), formatter);
        } catch (Exception e) {
            // 处理时间解析错误
            throw new IllegalArgumentException("时间格式不正确，正确格式为 yyyy-MM-dd HH:mm:ss", e);
        } //将字符串转换为 LocalDateTime
        EcoChainAnalyticalOverviewReturnParamOne returnParam = new EcoChainAnalyticalOverviewReturnParamOne();
        if ("1".equals(getUserInfo.getUserType())||"-1".equals(getUserInfo.getUserType())){
            LambdaQueryWrapper<EcoChainDataStatistics> queryWrapper = new LambdaQueryWrapper<>();
            if (ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode())&&"1".equals(getUserInfo.getUserType())) {
                queryWrapper.eq(EcoChainDataStatistics::getSocialCreditCode, getUserInfo.getSocialCreditCode());
            }
            queryWrapper.between(EcoChainDataStatistics::getAddDatetime, startDateTime, endDateTime);
            List<EcoChainDataStatistics> allList = ecoChainDataStatisticsMapper.selectList(queryWrapper);
            Map<String, Long> typeCountMap = allList.stream() //统计各类访问量
                    .collect(Collectors.groupingBy(EcoChainDataStatistics::getStatisticalType, Collectors.counting()));
            returnParam.setEcoChainScanVisitNum(typeCountMap.getOrDefault(ECO_CHAIN_SCAN, 0L));
            returnParam.setEnterpriseScanVisitNum(typeCountMap.getOrDefault(COMPANY_SCAN, 0L));
            returnParam.setProductScanNum(typeCountMap.getOrDefault(PRODUCT_SCAN, 0L));
            returnParam.setShareNumberNum(typeCountMap.getOrDefault(SHARE, 0L));
            returnParam.setBrandStoryNum(typeCountMap.getOrDefault(BRAND_STORY, 0L));
            returnParam.setBasicInfoNum(typeCountMap.getOrDefault(BASIC_INFO, 0L));
            returnParam.setHonorQualificationNum(typeCountMap.getOrDefault(HONORS_QUALIFICATIONS, 0L));
            returnParam.setVideoMonitoringNum(typeCountMap.getOrDefault(VIDEO_SURVEILLANCE, 0L));
            returnParam.setHomePageNum(typeCountMap.getOrDefault(INDEX, 0L));
            returnParam.setProductCenterNum(typeCountMap.getOrDefault(PRODUCT_CENTER, 0L));
            returnParam.setContactBossNum(typeCountMap.getOrDefault(CONTACT_BOSS, 0L));
            returnParam.setMerchantCommitmentNum(typeCountMap.getOrDefault(MERCHANT_PROMISE, 0L));
            returnParam.setBusinessCardShareBrowseNum(typeCountMap.getOrDefault(BUSINESS_CARD_SHARE_BROWSE, 0L));
            returnParam.setProductShareBrowseNum(typeCountMap.getOrDefault(PRODUCT_SHARE_BROWSE, 0L));
            returnParam.setCompanyHomepageShareBrowseNum(typeCountMap.getOrDefault(COMPANY_HOMEPAGE_SHARE_BROWSE, 0L));
            Long totalVisitorCount = returnParam.getEcoChainScanVisitNum()
                    + returnParam.getEnterpriseScanVisitNum()
                    + returnParam.getProductScanNum()
                    + returnParam.getBusinessCardShareBrowseNum()
                    + returnParam.getProductShareBrowseNum()
                    + returnParam.getCompanyHomepageShareBrowseNum();
            returnParam.setTotalVisitorCount(totalVisitorCount);
            // 判断时间范围是否在同一天
            if (startDateTime.toLocalDate().isEqual(endDateTime.toLocalDate())) {
                // 同一天，按小时统计
                List<EcoChainDataStatisticsNameValuePairParam> hourlyCounts = calculateHourlyCounts(allList, startDateTime.toLocalDate());
                returnParam.setHourlyVisitorCounts(hourlyCounts);
            } else {
                // 跨天，按天统计
                List<EcoChainDataStatisticsNameValuePairParam> dailyCounts = calculateDailyCounts(allList, startDateTime.toLocalDate(), endDateTime.toLocalDate());
                returnParam.setDailyVisitorCounts(dailyCounts);
            }
            return returnParam;
        }else if("0".equals(getUserInfo.getUserType())){
            MyLambdaQueryWrapper<EcoChainDataStatistics> queryWrapper = new MyLambdaQueryWrapper<>();

            /**
             * 管辖区域
             *         1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
             *         如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
             *         当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
             *         最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
             *         2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
             */
            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                // 获取当前用户的管辖区域列表
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    // 如果传入的areaId不合法，直接返回空的页面
                    if (!list.contains(param.getAreaId())) {
                        return null;
                    }
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        queryWrapper.eq(EcoChainDataStatistics::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainDataStatistics::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainDataStatistics::getAreaId, list);
                }
            }
            queryWrapper.between(EcoChainDataStatistics::getAddDatetime, startDateTime, endDateTime);
            List<EcoChainDataStatistics> allList = ecoChainDataStatisticsMapper.selectList(queryWrapper);
            Map<String, Long> typeCountMap = allList.stream() //统计各类访问量
                    .collect(Collectors.groupingBy(EcoChainDataStatistics::getStatisticalType, Collectors.counting()));
            returnParam.setEcoChainScanVisitNum(typeCountMap.getOrDefault(ECO_CHAIN_SCAN, 0L));
            returnParam.setEnterpriseScanVisitNum(typeCountMap.getOrDefault(COMPANY_SCAN, 0L));
            returnParam.setProductScanNum(typeCountMap.getOrDefault(PRODUCT_SCAN, 0L));
            returnParam.setShareNumberNum(typeCountMap.getOrDefault(SHARE, 0L));
            returnParam.setBrandStoryNum(typeCountMap.getOrDefault(BRAND_STORY, 0L));
            returnParam.setBasicInfoNum(typeCountMap.getOrDefault(BASIC_INFO, 0L));
            returnParam.setHonorQualificationNum(typeCountMap.getOrDefault(HONORS_QUALIFICATIONS, 0L));
            returnParam.setVideoMonitoringNum(typeCountMap.getOrDefault(VIDEO_SURVEILLANCE, 0L));
            returnParam.setHomePageNum(typeCountMap.getOrDefault(INDEX, 0L));
            returnParam.setProductCenterNum(typeCountMap.getOrDefault(PRODUCT_CENTER, 0L));
            returnParam.setContactBossNum(typeCountMap.getOrDefault(CONTACT_BOSS, 0L));
            returnParam.setMerchantCommitmentNum(typeCountMap.getOrDefault(MERCHANT_PROMISE, 0L));
            returnParam.setBusinessCardShareBrowseNum(typeCountMap.getOrDefault(BUSINESS_CARD_SHARE_BROWSE, 0L));
            returnParam.setProductShareBrowseNum(typeCountMap.getOrDefault(PRODUCT_SHARE_BROWSE, 0L));
            returnParam.setCompanyHomepageShareBrowseNum(typeCountMap.getOrDefault(COMPANY_HOMEPAGE_SHARE_BROWSE, 0L));
            Long totalVisitorCount = returnParam.getEcoChainScanVisitNum()
                    + returnParam.getEnterpriseScanVisitNum()
                    + returnParam.getProductScanNum()
                    + returnParam.getBusinessCardShareBrowseNum()
                    + returnParam.getProductShareBrowseNum()
                    + returnParam.getCompanyHomepageShareBrowseNum();
            returnParam.setTotalVisitorCount(totalVisitorCount);
            // 判断时间范围是否在同一天
            if (startDateTime.toLocalDate().isEqual(endDateTime.toLocalDate())) {
                // 同一天，按小时统计
                List<EcoChainDataStatisticsNameValuePairParam> hourlyCounts = calculateHourlyCounts(allList, startDateTime.toLocalDate());
                returnParam.setHourlyVisitorCounts(hourlyCounts);
            } else {
                // 跨天，按天统计
                List<EcoChainDataStatisticsNameValuePairParam> dailyCounts = calculateDailyCounts(allList, startDateTime.toLocalDate(), endDateTime.toLocalDate());
                returnParam.setDailyVisitorCounts(dailyCounts);
            }
            return returnParam;
        }
        return returnParam;
    }

    private List<EcoChainDataStatisticsNameValuePairParam> calculateHourlyCounts(List<EcoChainDataStatistics> allList, LocalDate date) {
        // 需要按小时统计的类型
        List<String> hourlyTypes = Arrays.asList(ECO_CHAIN_SCAN, COMPANY_SCAN, PRODUCT_SCAN, BUSINESS_CARD_SHARE_BROWSE, PRODUCT_SHARE_BROWSE, COMPANY_HOMEPAGE_SHARE_BROWSE);

        // 过滤出需要统计的类型，并且在指定日期
        List<EcoChainDataStatistics> filteredList = allList.stream()
                .filter(data -> hourlyTypes.contains(data.getStatisticalType())
                        && data.getAddDatetime().toLocalDate().isEqual(date))
                .collect(Collectors.toList());

        // 按小时分组并统计
        Map<Integer, Long> hourlyCount = filteredList.stream()
                .collect(Collectors.groupingBy(
                        data -> data.getAddDatetime().getHour(),
                        Collectors.counting()
                ));

        // 初始化所有小时的统计为0，并转换为列表格式
        List<EcoChainDataStatisticsNameValuePairParam> completeHourlyCount = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            String hourName = hour + "点";
            Long count = hourlyCount.getOrDefault(hour, 0L);
            completeHourlyCount.add(new EcoChainDataStatisticsNameValuePairParam(hourName, count));
        }

        return completeHourlyCount;
    }


    private List<EcoChainDataStatisticsNameValuePairParam> calculateDailyCounts(List<EcoChainDataStatistics> allList, LocalDate startDate, LocalDate endDate) {
        // 需要按天统计的类型
        List<String> dailyTypes = Arrays.asList(ECO_CHAIN_SCAN, COMPANY_SCAN, PRODUCT_SCAN, BUSINESS_CARD_SHARE_BROWSE, PRODUCT_SHARE_BROWSE, COMPANY_HOMEPAGE_SHARE_BROWSE);

        // 过滤出需要统计的类型，并且在指定日期范围内
        List<EcoChainDataStatistics> filteredList = allList.stream()
                .filter(data -> dailyTypes.contains(data.getStatisticalType())
                        && !data.getAddDatetime().toLocalDate().isBefore(startDate)
                        && !data.getAddDatetime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());

        // 按天分组并统计
        Map<LocalDate, Long> dailyCount = filteredList.stream()
                .collect(Collectors.groupingBy(
                        data -> data.getAddDatetime().toLocalDate(),
                        Collectors.counting()
                ));

        // 初始化所有日期的统计为0，并转换为列表格式
        List<EcoChainDataStatisticsNameValuePairParam> completeDailyCount = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String dateName = currentDate.toString();
            Long count = dailyCount.getOrDefault(currentDate, 0L);
            completeDailyCount.add(new EcoChainDataStatisticsNameValuePairParam(dateName, count));
            currentDate = currentDate.plusDays(1);
        }

        return completeDailyCount;
    }


    @Override
    public Page<EcoChainPotentialCustomerPageReturnParam> selectPotentialCustomer(EcoChainPotentialCustomerPageSearchParam param) {
        // 创建分页对象
        Page<EcoChainDataStatistics> page = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<EcoChainDataStatistics> queryWrapper = new LambdaQueryWrapper<>();

        // 政府人员或二级管理员用户登录，查询管辖区域内数据
        if ("0".equals(getUserInfo.getUserType())) {
            /**
             * 管辖区域
             *         1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
             *         如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
             *         当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
             *         最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
             *         2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
             */
            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                // 获取当前用户的管辖区域列表
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    // 如果传入的areaId不合法，直接返回空的页面
                    if (!list.contains(param.getAreaId())) {
                        return new Page<>();
                    }
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        queryWrapper.eq(EcoChainDataStatistics::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainDataStatistics::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainDataStatistics::getAreaId, list);
                }
            }

        }

        // 本企业人员登录
        if ("1".equals(getUserInfo.getUserType())) {
            queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()), EcoChainDataStatistics::getSocialCreditCode, getUserInfo.getSocialCreditCode());
        }

        queryWrapper.isNotNull(EcoChainDataStatistics::getPhoneNumber)
                .select(EcoChainDataStatistics::getPhoneNumber) // 仅选择phoneNumber
                .groupBy(EcoChainDataStatistics::getPhoneNumber); // 按phoneNumber分组

        Page<EcoChainDataStatistics> pageResult = this.page(page, queryWrapper);

        // 获取所有phoneNumber
        List<String> phoneNumbers = pageResult.getRecords().stream()
                .map(EcoChainDataStatistics::getPhoneNumber)
                .collect(Collectors.toList());

        // 根据phoneNumber获取最新的记录
        List<EcoChainDataStatistics> latestRecords = new ArrayList<>();
        for (String phoneNumber : phoneNumbers) {
            LambdaQueryWrapper<EcoChainDataStatistics> latestRecordQuery = new LambdaQueryWrapper<>();
            latestRecordQuery.eq(EcoChainDataStatistics::getPhoneNumber, phoneNumber)
                    .orderByDesc(EcoChainDataStatistics::getOperationTime) // 按新增时间降序排列
                    .last("LIMIT 1"); // 限制只取最新的记录
            EcoChainDataStatistics latestRecord = this.getOne(latestRecordQuery);
            if (latestRecord != null) {
                latestRecords.add(latestRecord);
            }
        }

        // 转换为EcoChainPotentialCustomerPageReturnParam列表
        List<EcoChainPotentialCustomerPageReturnParam> returnParams = latestRecords.stream().map(statistics -> {
            EcoChainPotentialCustomerPageReturnParam returnParam = new EcoChainPotentialCustomerPageReturnParam();
            returnParam.setWechatAvatar(statistics.getWechatAvatar())
                    .setWechatName(statistics.getWechatName())
                    .setPhoneNumber(statistics.getPhoneNumber())
                    .setTotal(countByWechatAvatarAndWechatNameAndSocialCreditCode(param, statistics.getWechatAvatar(), statistics.getWechatName(), statistics.getSocialCreditCode(), statistics.getPhoneNumber()))
                    .setSocialCreditCode(statistics.getSocialCreditCode());
            return returnParam;
        }).collect(Collectors.toList());

        // 创建返回的Page对象
        Page<EcoChainPotentialCustomerPageReturnParam> resultPage = new Page<>();
        resultPage.setRecords(returnParams);
        resultPage.setTotal(latestRecords.size()); // 设置总记录数为最新记录的数量
        resultPage.setSize(page.getSize());
        resultPage.setCurrent(page.getCurrent());

        return resultPage;
    }

    EcoChainDataStatistics getLatestRecordByPhoneNumber(List<EcoChainDataStatistics> records) {
        return records.stream()
                .max(Comparator.comparing(EcoChainDataStatistics::getOperationTime))
                .orElse(null);
    }

    @Override
    public Page<EcoChainProductAnalyticalPageReturnParam> productAnalyticalPage(EcoChainProductAnalyticalPageSearchParam param) {
        LambdaQueryWrapper<EcoChainDataStatistics> queryWrapper = new LambdaQueryWrapper<>();
        // 添加权限：
        // 一、超级管理员用户登录，查询全部
        if ("-1".equals(getUserInfo.getUserType())) {

        }
        // 二、政府人员或二级管理员用户登录，查询管辖区域内数据
        if ("0".equals(getUserInfo.getUserType())) {
            /**
             * 管辖区域
             *         1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
             *         如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
             *         当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
             *         最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
             *         2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
             */
            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                // 获取当前用户的管辖区域列表
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    // 如果传入的areaId不合法，直接返回空的页面
                    if (!list.contains(param.getAreaId())) {
                        return new Page<>();
                    }
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        queryWrapper.eq(EcoChainDataStatistics::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainDataStatistics::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainDataStatistics::getAreaId, list);
                }
            }

        }
        // 三、本企业人员登录
        if ("1".equals(getUserInfo.getUserType())) {
            queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainDataStatistics::getSocialCreditCode, getUserInfo.getSocialCreditCode());
        }

        queryWrapper
                .ge(ObjectUtils.isNotEmpty(param.getStartTime()),EcoChainDataStatistics::getAddDatetime ,param.getStartTime())
                .lt(ObjectUtils.isNotEmpty(param.getEndTime()),EcoChainDataStatistics::getAddDatetime ,param.getEndTime());

        queryWrapper.select(EcoChainDataStatistics::getEcoChainProductManagerId)
                .groupBy(EcoChainDataStatistics::getEcoChainProductManagerId);

        List<EcoChainDataStatistics> list = ecoChainDataStatisticsMapper.selectList(queryWrapper);

        List<EcoChainProductAnalyticalPageReturnParam> resultList = new ArrayList<>();
        for (EcoChainDataStatistics eachDataStatistics : list) {
            if (eachDataStatistics == null) {
                continue;
            }
            if (eachDataStatistics.getEcoChainProductManagerId() == null || "0L".equals(eachDataStatistics.getEcoChainProductManagerId())) {
                continue;
            }
            EcoChainProductAnalyticalPageReturnParam returnParam = new EcoChainProductAnalyticalPageReturnParam();
            returnParam.setEcoChainProductManagerId(eachDataStatistics.getEcoChainProductManagerId());

            // 查询产品信息
            EcoChainProductManager productManager = ecoChainProductManagerService.getById(eachDataStatistics.getEcoChainProductManagerId());
            if (productManager == null) {
                // 查询不到该产品信息
                continue;
            }
            if (StringUtils.isNotBlank(productManager.getProductName())) {
                returnParam.setProductName(productManager.getProductName());
            }

            // 查询产品分类信息
            String productClassificationIds = productManager.getEcoChainProductClassificationId();
            if (StringUtils.isNotBlank(productClassificationIds)) {
                Long productClassificationId = 0L;
                returnParam.setEcoChainProductClassificationId(productClassificationIds);
                productClassificationIds = productClassificationIds.substring(1, productClassificationIds.length() - 1);
                if (productClassificationIds.contains(",")) {
                    String[] split = productClassificationIds.split(",");
                    productClassificationId = Long.parseLong(split[0]);
                } else {
                    productClassificationId = Long.parseLong(productClassificationIds);
                }

                EcoChainProductClassification productClassification = ecoChainProductClassificationService.getById(productClassificationId);
                if (ObjectUtils.isNotEmpty(productClassification)) {
                    String productClassificationName = productClassification.getProductClassificationName();
                    returnParam.setProductClassificationName(productClassificationName);
                }
            }

            // 查询产品扫码（浏览量）数量
            long productScanCount = this.count(new LambdaQueryWrapper<EcoChainDataStatistics>()
                    .eq(EcoChainDataStatistics::getEcoChainProductManagerId, eachDataStatistics.getEcoChainProductManagerId())
                    .eq(EcoChainDataStatistics::getStatisticalType, "产品扫码")
                    .ge(ObjectUtils.isNotEmpty(param.getStartTime()),EcoChainDataStatistics::getAddDatetime ,param.getStartTime())
                    .lt(ObjectUtils.isNotEmpty(param.getEndTime()),EcoChainDataStatistics::getAddDatetime ,param.getEndTime())
            );
            returnParam.setProductScan(productScanCount);

            // 查询产品分享次数
            long shareNumber = this.count(new LambdaQueryWrapper<EcoChainDataStatistics>()
                    .eq(EcoChainDataStatistics::getEcoChainProductManagerId, eachDataStatistics.getEcoChainProductManagerId())
                    .eq(EcoChainDataStatistics::getStatisticalType, "分享")
                    .ge(ObjectUtils.isNotEmpty(param.getStartTime()),EcoChainDataStatistics::getAddDatetime ,param.getStartTime())
                    .lt(ObjectUtils.isNotEmpty(param.getEndTime()),EcoChainDataStatistics::getAddDatetime ,param.getEndTime())
            );
            returnParam.setShareNumber(shareNumber);

            // 查询产品分享次数
            long productShareNumber = this.count(new LambdaQueryWrapper<EcoChainDataStatistics>()
                    .eq(EcoChainDataStatistics::getEcoChainProductManagerId, eachDataStatistics.getEcoChainProductManagerId())
                    .eq(EcoChainDataStatistics::getStatisticalType, "产品分享浏览")
                    .ge(ObjectUtils.isNotEmpty(param.getStartTime()),EcoChainDataStatistics::getAddDatetime ,param.getStartTime())
                    .lt(ObjectUtils.isNotEmpty(param.getEndTime()),EcoChainDataStatistics::getAddDatetime ,param.getEndTime())
            );
            returnParam.setProductShareNumber(productShareNumber);

            resultList.add(returnParam);

        }
        // --此处需要手动设置分页
        // 计算当前页的起始索引
        int fromIndex = (int)((param.getCurrent() - 1) * param.getSize());
        int toIndex = (int)Math.min(fromIndex + param.getSize(), resultList.size());
        // 获取当前页的记录
        List<EcoChainProductAnalyticalPageReturnParam> currentPageRecords = resultList.subList(fromIndex, toIndex);
        // 创建分页对象
        Page<EcoChainProductAnalyticalPageReturnParam> pageResult = new Page<>(param.getCurrent(), param.getSize());
        pageResult.setTotal(resultList.size());
        pageResult.setRecords(currentPageRecords);

        return pageResult;
    }

    private Long countByWechatAvatarAndWechatNameAndSocialCreditCode(EcoChainPotentialCustomerPageSearchParam param, String wechatAvatar, String wechatName, String socialCreditCode, String phoneNumber) {
        LambdaQueryWrapper<EcoChainDataStatistics> countQuery = new LambdaQueryWrapper<>();
        countQuery.eq(EcoChainDataStatistics::getWechatAvatar, wechatAvatar)
                .eq(EcoChainDataStatistics::getWechatName, wechatName)
                .eq(EcoChainDataStatistics::getSocialCreditCode, socialCreditCode)
                .eq(EcoChainDataStatistics::getPhoneNumber, phoneNumber);
        if ("0".equals(getUserInfo.getUserType())) {
            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                // 获取当前用户的管辖区域列表
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        countQuery.eq(EcoChainDataStatistics::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        countQuery.in(EcoChainDataStatistics::getAreaId, list);
                    }
                } else {
                    countQuery.in(EcoChainDataStatistics::getAreaId, list);
                }
            }

        }
        return this.count(countQuery);
    }



    @Override
    public EcoChainProductAnalysisReturnParam productAnalyticalChart(EcoChainProductAnalyticalChartSearchParam param) {
        EcoChainProductAnalysisReturnParam returnParam = new EcoChainProductAnalysisReturnParam();
        List<EcoChainProductAnalyticalChartNameValueParam> hourlyVisitorCounts = new ArrayList<>();
        List<EcoChainProductAnalyticalChartNameValueParam> dailyVisitorCounts = new ArrayList<>();
        returnParam.setHourlyVisitorCounts(hourlyVisitorCounts);
        returnParam.setDailyVisitorCounts(dailyVisitorCounts);

        // 0.统计产品扫码信息
        LambdaQueryWrapper<EcoChainDataStatistics> queryWrapper = productAnalyticalChartPermission(param);
        queryWrapper
                .eq(param.getEcoChainProductManagerId() != null && param.getEcoChainProductManagerId() != 0l,
                        EcoChainDataStatistics::getEcoChainProductManagerId, param.getEcoChainProductManagerId())
                .eq(EcoChainDataStatistics::getStatisticalType, "产品扫码");
        List<EcoChainDataStatistics> listVisitorCounts = this.list(queryWrapper);
        // 0.1.这里必须要走，因为必须要运行type=0时的归零语句
        returnParam = Statistics(returnParam, param, listVisitorCounts, 0);


        // 1.统计分享次数信息
        LambdaQueryWrapper<EcoChainDataStatistics> queryWrapper1 = productAnalyticalChartPermission(param);
        queryWrapper1
                .eq(param.getEcoChainProductManagerId() != null && param.getEcoChainProductManagerId() != 0l,
                        EcoChainDataStatistics::getEcoChainProductManagerId, param.getEcoChainProductManagerId())
                .eq(EcoChainDataStatistics::getStatisticalType, "分享")
                .isNotNull(EcoChainDataStatistics::getEcoChainProductManagerId);
        List<EcoChainDataStatistics> listShareCounts = this.list(queryWrapper1);
        if (!listShareCounts.isEmpty()) {
            returnParam = Statistics(returnParam, param, listShareCounts, 1);
        }

        // 2.统计产品分享浏览信息
        LambdaQueryWrapper<EcoChainDataStatistics> queryWrapper2 = productAnalyticalChartPermission(param);
        queryWrapper2
                .eq(param.getEcoChainProductManagerId() != null && param.getEcoChainProductManagerId() != 0l,
                        EcoChainDataStatistics::getEcoChainProductManagerId, param.getEcoChainProductManagerId())
                .eq(EcoChainDataStatistics::getStatisticalType, "产品分享浏览");
        List<EcoChainDataStatistics> listProductShareCounts = this.list(queryWrapper2);
        if (!listProductShareCounts.isEmpty()) {
            returnParam = Statistics(returnParam, param, listProductShareCounts, 2);
        }

        return returnParam;

    }

    public EcoChainProductAnalysisReturnParam Statistics(EcoChainProductAnalysisReturnParam result, EcoChainProductAnalyticalChartSearchParam param,
                                                         List<EcoChainDataStatistics> list, int type) {
        // 获取时间范围，格式为yyyy-MM-dd HH:mm:ss
        String startTime = param.getStartTime();
        String endTime = param.getEndTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);

        boolean sameDay = startDateTime.toLocalDate().isEqual(endDateTime.toLocalDate());

        // 一、如过日期在同一日，统计当天不同时段的数据
        if (sameDay) {
            List<EcoChainProductAnalyticalChartNameValueParam> hourlyVisitorCounts = result.getHourlyVisitorCounts();
            if (type == 0) {
                for (int hour = 0; hour < 24; hour++) {
                    EcoChainProductAnalyticalChartNameValueParam chartNameValueParam = new EcoChainProductAnalyticalChartNameValueParam();
                    chartNameValueParam.setName(String.valueOf(hour));
                    chartNameValueParam.setVisitorCountsValue(0L);
                    chartNameValueParam.setShareCountsValue(0L);
                    chartNameValueParam.setProductShareCountsValue(0L);
                    hourlyVisitorCounts.add(chartNameValueParam);
                }
            }

            for (EcoChainDataStatistics dataStatistics : list) {
                if (dataStatistics.getAddDatetime().toLocalDate().isEqual(startDateTime.toLocalDate())) {
                    int hour = dataStatistics.getAddDatetime().getHour();

                    // chartNameValueParam中name为hourStr的对应的visitorCountsValue值+1
                    if (type == 0) {
                        EcoChainProductAnalyticalChartNameValueParam analyticalChartNameValueParam = hourlyVisitorCounts.get(hour);
                        analyticalChartNameValueParam.setVisitorCountsValue(analyticalChartNameValueParam.getVisitorCountsValue() + 1l);
                    } else if (type == 1) {
                        EcoChainProductAnalyticalChartNameValueParam analyticalChartNameValueParam = hourlyVisitorCounts.get(hour);
                        analyticalChartNameValueParam.setShareCountsValue(analyticalChartNameValueParam.getShareCountsValue() + 1l);
                    } else if(type == 2) {
                        EcoChainProductAnalyticalChartNameValueParam analyticalChartNameValueParam = hourlyVisitorCounts.get(hour);
                        analyticalChartNameValueParam.setProductShareCountsValue(analyticalChartNameValueParam.getProductShareCountsValue() + 1l);
                    }
                }
            }
        }

        // 二、如过日期不在同一日，统计不同日期的数据
        if (!sameDay) {
            List<EcoChainProductAnalyticalChartNameValueParam> dailyVisitorCounts = result.getDailyVisitorCounts();
            if (type == 0) {
                for (LocalDateTime date = startDateTime; !date.isAfter(endDateTime); date = date.plusDays(1)) {
                    String dateStr = date.toLocalDate().toString();
                    EcoChainProductAnalyticalChartNameValueParam chartNameValueParam = new EcoChainProductAnalyticalChartNameValueParam();

                    chartNameValueParam.setName(dateStr);
                    chartNameValueParam.setVisitorCountsValue(0L);
                    chartNameValueParam.setShareCountsValue(0L);
                    chartNameValueParam.setProductShareCountsValue(0L);
                    dailyVisitorCounts.add(chartNameValueParam);

                }
            }

            int index = 0;
            LocalDateTime currentDate = startDateTime;
            // 遍历日期，startDateTime - endDateTime
            while (currentDate.toLocalDate().isBefore(endDateTime.toLocalDate()) ||
                    currentDate.toLocalDate().isEqual(endDateTime.toLocalDate())) {
                LocalDate currentLocalDate = currentDate.toLocalDate();

                // 检查每个数据统计的日期
                for (EcoChainDataStatistics eachDataStatistics : list) {
                    LocalDateTime addDatetime = eachDataStatistics.getAddDatetime();
                    // 如果addDatetime的日期与当前遍历日期相同，则计数加1
                    if (addDatetime.toLocalDate().isEqual(currentLocalDate)) {
                        if (type == 0) {
                            EcoChainProductAnalyticalChartNameValueParam analyticalChartNameValueParam = dailyVisitorCounts.get(index);
                            analyticalChartNameValueParam.setVisitorCountsValue(analyticalChartNameValueParam.getVisitorCountsValue() + 1l);
                        } else if (type == 1) {
                            EcoChainProductAnalyticalChartNameValueParam analyticalChartNameValueParam = dailyVisitorCounts.get(index);
                            analyticalChartNameValueParam.setShareCountsValue(analyticalChartNameValueParam.getShareCountsValue() + 1l);
                        } else if(type == 2) {
                            EcoChainProductAnalyticalChartNameValueParam analyticalChartNameValueParam = dailyVisitorCounts.get(index);
                            analyticalChartNameValueParam.setProductShareCountsValue(analyticalChartNameValueParam.getProductShareCountsValue() + 1l);
                        }

                    }
                }
                // 前进到下一天
                currentDate = currentDate.plusDays(1);
                index++;
            }
        }

        return result;
    }

    public LambdaQueryWrapper<EcoChainDataStatistics> productAnalyticalChartPermission(
            EcoChainProductAnalyticalChartSearchParam param) {

        LambdaQueryWrapper<EcoChainDataStatistics> queryWrapper = new LambdaQueryWrapper<>();
        // 添加权限：
        // 一、超级管理员用户登录，查询全部
        if ("-1".equals(getUserInfo.getUserType())) {

        }
        // 二、政府人员或二级管理员用户登录，查询管辖区域内数据
        if ("0".equals(getUserInfo.getUserType())) {
            /**
             * 管辖区域
             *         1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
             *         如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
             *         当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
             *         最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
             *         2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
             */
            if (ObjectUtils.isNotEmpty(param.getIsApplicableArea()) && param.getIsApplicableArea() == 0) {
                // 获取当前用户的管辖区域列表
                List<Long> list = peopleGovernmentService.getManageAreaIdList();
                if (ObjectUtils.isNotEmpty(param.getAreaId())) {
                    // 如果传入的areaId不合法，直接返回空的页面
                    if (!list.contains(param.getAreaId())) {
                        return new LambdaQueryWrapper<>();
                    }
                    // 当传入的areaId合法时
                    // 当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据
                    if (ObjectUtils.isEmpty(param.getFlag()) || param.getFlag() == 0) {
                        queryWrapper.eq(EcoChainDataStatistics::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainDataStatistics::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainDataStatistics::getAreaId, list);
                }
            }

        }
        // 三、本企业人员登录
        if ("1".equals(getUserInfo.getUserType())) {
            queryWrapper.eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                    EcoChainDataStatistics::getSocialCreditCode, getUserInfo.getSocialCreditCode());
        }

        return queryWrapper;
    }

    @Override
    public EcoChainAnalyticalOverviewReturnParamMiniProgram selectAnalyticalOverviewMiniProgram(EcoChainAnalyticalOverviewSearchParam param) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = LocalDateTime.parse(param.getStartTime(), formatter);
            endDateTime = LocalDateTime.parse(param.getEndTime(), formatter);
        } catch (Exception e) {
            // 处理时间解析错误
            throw new IllegalArgumentException("时间格式不正确，正确格式为 yyyy-MM-dd HH:mm:ss", e);
        } //将字符串转换为 LocalDateTime
        EcoChainAnalyticalOverviewReturnParamMiniProgram returnParam = new EcoChainAnalyticalOverviewReturnParamMiniProgram();
        LambdaQueryWrapper<EcoChainDataStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(EcoChainDataStatistics::getAddDatetime, startDateTime, endDateTime);
        if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())) {
            queryWrapper.eq(ObjectUtils.isNotEmpty(param.getSocialCreditCode()), EcoChainDataStatistics::getSocialCreditCode,param.getSocialCreditCode());
        } else {
            queryWrapper.eq(EcoChainDataStatistics::getSocialCreditCode, getUserInfo.getSocialCreditCode());
        }
        List<EcoChainDataStatistics> allList = ecoChainDataStatisticsMapper.selectList(queryWrapper);
        Map<String, Long> typeCountMap = allList.stream() //统计各类访问量
                .collect(Collectors.groupingBy(EcoChainDataStatistics::getStatisticalType, Collectors.counting()));
        returnParam.setEcoChainScanVisitNum(typeCountMap.getOrDefault(ECO_CHAIN_SCAN, 0L));
        returnParam.setEnterpriseScanVisitNum(typeCountMap.getOrDefault(COMPANY_SCAN, 0L));
        returnParam.setProductScanNum(typeCountMap.getOrDefault(PRODUCT_SCAN, 0L));
        returnParam.setShareNumberNum(typeCountMap.getOrDefault(SHARE, 0L));
        returnParam.setBrandStoryNum(typeCountMap.getOrDefault(BRAND_STORY, 0L));
        returnParam.setBasicInfoNum(typeCountMap.getOrDefault(BASIC_INFO, 0L));
        returnParam.setHonorQualificationNum(typeCountMap.getOrDefault(HONORS_QUALIFICATIONS, 0L));
        returnParam.setVideoMonitoringNum(typeCountMap.getOrDefault(VIDEO_SURVEILLANCE, 0L));
        returnParam.setHomePageNum(typeCountMap.getOrDefault(INDEX, 0L));
        returnParam.setProductCenterNum(typeCountMap.getOrDefault(PRODUCT_CENTER, 0L));
        returnParam.setContactBossNum(typeCountMap.getOrDefault(CONTACT_BOSS, 0L));
        returnParam.setMerchantCommitmentNum(typeCountMap.getOrDefault(MERCHANT_PROMISE, 0L));
        returnParam.setBusinessCardShareBrowseNum(typeCountMap.getOrDefault(BUSINESS_CARD_SHARE_BROWSE, 0L));
        returnParam.setProductShareBrowseNum(typeCountMap.getOrDefault(PRODUCT_SHARE_BROWSE, 0L));
        returnParam.setCompanyHomepageShareBrowseNum(typeCountMap.getOrDefault(COMPANY_HOMEPAGE_SHARE_BROWSE, 0L));
        Long totalVisitorCount = returnParam.getEcoChainScanVisitNum()
                + returnParam.getEnterpriseScanVisitNum()
                + returnParam.getProductScanNum()
                + returnParam.getBusinessCardShareBrowseNum()
                + returnParam.getProductShareBrowseNum()
                + returnParam.getCompanyHomepageShareBrowseNum();
        returnParam.setTotalVisitorCount(totalVisitorCount);
        return returnParam;
    }
}
