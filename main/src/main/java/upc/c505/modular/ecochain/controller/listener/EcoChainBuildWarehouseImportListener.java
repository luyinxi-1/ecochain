package upc.c505.modular.ecochain.controller.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import upc.c505.modular.ecochain.controller.param.excelparam.EcoChainBuildWarehouseImportParam;
import upc.c505.modular.ecochain.entity.EcoChainBuildWarehouse;
import upc.c505.modular.ecochain.entity.EcoChainRegionalConfiguration;
import upc.c505.modular.ecochain.entity.EcoChainTypeConfiguration;
import upc.c505.modular.ecochain.service.IEcoChainBuildWarehouseService;
import upc.c505.modular.people.entity.PeopleEnterprise;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description: 生态链建库导入监听器
 * @Author: xth
 * @Date: 2026/3/16
 */
@Slf4j
public class EcoChainBuildWarehouseImportListener extends AnalysisEventListener<EcoChainBuildWarehouseImportParam> {

    private final IEcoChainBuildWarehouseService ecoChainBuildWarehouseService;
    
    private final Map<String, EcoChainTypeConfiguration> typeConfigMap;
    
    private final Map<String, EcoChainRegionalConfiguration> regionalConfigMap;
    
    private final Map<String, PeopleEnterprise> enterpriseMap;
    
    // 当前登录用户的企业名称和社会信用代码
    private final String currentEnterpriseName;
    private final String currentSocialCreditCode;

    private Integer numOfNew = 0;

    private static final int BATCH_COUNT = 1000;

    List<EcoChainBuildWarehouse> list = new ArrayList<>();
    
    // 内存计数器，用于生成批号
    private Long batchNumberCounter = 0L;

    public EcoChainBuildWarehouseImportListener(IEcoChainBuildWarehouseService ecoChainBuildWarehouseService, 
                                                 Map<String, EcoChainTypeConfiguration> typeConfigMap,
                                                 Map<String, EcoChainRegionalConfiguration> regionalConfigMap,
                                                 Map<String, PeopleEnterprise> enterpriseMap,
                                                 String currentEnterpriseName,
                                                 String currentSocialCreditCode) {
        this.ecoChainBuildWarehouseService = ecoChainBuildWarehouseService;
        this.typeConfigMap = typeConfigMap;
        this.regionalConfigMap = regionalConfigMap;
        this.enterpriseMap = enterpriseMap;
        this.currentEnterpriseName = currentEnterpriseName;
        this.currentSocialCreditCode = currentSocialCreditCode;
        
        // 初始化计数器：查询当天该企业已有的记录数
        LocalDate currentDate = LocalDate.now();
        LocalDateTime startOfDay = currentDate.atStartOfDay();
        LocalDateTime endOfDay = currentDate.atTime(23, 59, 59);
        batchNumberCounter = ecoChainBuildWarehouseService.count(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EcoChainBuildWarehouse>()
                .eq(EcoChainBuildWarehouse::getSocialCreditCode, currentSocialCreditCode)
                .ge(EcoChainBuildWarehouse::getAddDatetime, startOfDay)
                .le(EcoChainBuildWarehouse::getAddDatetime, endOfDay));
    }

    @Override
    public void invoke(EcoChainBuildWarehouseImportParam data, AnalysisContext context) {
  /*      log.info("\n========== 读取到一条数据 ==========");
        log.info("原始 data 对象：{}", data.toString());
        log.info("  - 类型 (detailType): '{}'", data.getDetailType());
        log.info("  - 所属所 (industryGroup): '{}'", data.getIndustryGroup());
        log.info("  - 项目名称 (industryWarehouse): '{}'", data.getIndustryWarehouse());
        log.info("  - 联系人 (warahouseContact): '{}'", data.getWarahouseContact());
        log.info("  - 联系电话 (warahousePhone): '{}'", data.getWarahousePhone());
        log.info("  - 经营地址 (notes): '{}'", data.getNotes());
        log.info("  - 门头招牌名称 (shopSignName): '{}'", data.getShopSignName());
        log.info("  - 监管所联系人 (supervisorContact): '{}'", data.getSupervisorContact());
        log.info("  - 监管所联系电话 (supervisorPhone): '{}'", data.getSupervisorPhone());
        log.info("  - 可见人 (visiblePeopleName): '{}'", data.getVisiblePeopleName());
        log.info("======================================\n");*/
        
        if (data.getDetailType() == null && data.getIndustryWarehouse() == null) {
            log.error("⚠️ 警告：所有字段都为 NULL！可能是 Excel 表头不匹配！");
            log.error("请检查 Excel 表头是否与以下完全一致（包括空格）：");
            log.error("  类型、所属所、项目名称、联系人、联系电话、经营地址、门头招牌名称、监管所联系人、监管所联系电话、可见人");
        }
        
        EcoChainBuildWarehouse warehouse = new EcoChainBuildWarehouse();
        
        // 根据类型查询 eco_chain_type_configuration 表填入 detailTypeOption 和 ecoChainTypeConfigurationId
        EcoChainTypeConfiguration typeConfig = typeConfigMap.get(data.getDetailType());
        if (typeConfig != null) {
            warehouse.setDetailTypeOption(typeConfig.getDetailType());
            warehouse.setEcoChainTypeConfigurationId(typeConfig.getId());
            //log.info("✅ 类型匹配成功：{} -> {}", data.getDetailType(), typeConfig.getDetailType());
        } else {
            log.warn("❌ 类型未找到：{}，请检查 eco_chain_type_configuration 表中是否有此 detail_type", data.getDetailType());
        }
        
        // 根据 Excel 中的所属所字段查询 eco_chain_regional_configuration 表
        if (data.getIndustryGroup() != null && !data.getIndustryGroup().isEmpty()) {
            EcoChainRegionalConfiguration regionalConfig = findRegionalConfigByIndustryGroup(data.getIndustryGroup());
            if (regionalConfig != null) {
                warehouse.setIndustryGroupOption(regionalConfig.getIndustryGroup());
                warehouse.setEcoChainRegionalConfigurationId(regionalConfig.getId());
                log.info("✅ 所属所匹配成功：{} -> {}", data.getIndustryGroup(), regionalConfig.getIndustryGroup());
            } else {
                log.warn("❌ 所属所未找到：{}，请检查 eco_chain_regional_configuration 表中是否有包含此名称的 industry_group", data.getIndustryGroup());
            }
        } else {
            log.warn("⚠️ Excel 中所属所为空");
        }
        
        // 项目名称（从 Excel 读取）
        warehouse.setIndustryWarehouse(data.getIndustryWarehouse());
        
        // 联系人（从 Excel 读取）
        warehouse.setWarahouseContact(data.getWarahouseContact());
        
        // 联系电话（从 Excel 读取）
        warehouse.setWarahousePhone(data.getWarahousePhone());
        
        // 经营地址
        warehouse.setNotes(data.getNotes());
        
        // 备注：将门头招牌名称、监管所联系人、监管所联系电话这三个字段拼起来
        String otherDescription = "";
        if (data.getShopSignName() != null && !data.getShopSignName().isEmpty()) {
            otherDescription += data.getShopSignName();
        }
        if (data.getSupervisorContact() != null && !data.getSupervisorContact().isEmpty()) {
            if (!otherDescription.isEmpty()) {
                otherDescription += ";";
            }
            otherDescription += data.getSupervisorContact();
        }
        if (data.getSupervisorPhone() != null && !data.getSupervisorPhone().isEmpty()) {
            if (!otherDescription.isEmpty()) {
                otherDescription += ";";
            }
            otherDescription += data.getSupervisorPhone();
        }
        warehouse.setOtherDescription(otherDescription);
    //    log.info("📝 拼接的备注：{}", otherDescription);
        
        // 可见人：根据姓名查询 people-enterprise 表，姓名一致时填入 visiblePeople 和 visiblePeopleName
        // 可见人可能有多个，支持中文逗号（，）或英文逗号（,）分隔
        if (data.getVisiblePeopleName() != null && !data.getVisiblePeopleName().isEmpty()) {
            // 先替换中文逗号为英文逗号，然后分割
            String visiblePeopleStr = data.getVisiblePeopleName().replace("，", ",");
            String[] visiblePeopleNames = visiblePeopleStr.split(",");
            StringBuilder visiblePeopleIds = new StringBuilder();
            StringBuilder visiblePeopleNamesResult = new StringBuilder();
            
            for (String name : visiblePeopleNames) {
                String trimmedName = name.trim();
                if (!trimmedName.isEmpty()) {
                    PeopleEnterprise enterprise = enterpriseMap.get(trimmedName);
                    if (enterprise != null) {
                        if (visiblePeopleIds.length() > 0) {
                            visiblePeopleIds.append(",");
                            visiblePeopleNamesResult.append(",");
                        }
                        visiblePeopleIds.append(enterprise.getId());
                        visiblePeopleNamesResult.append(trimmedName);
                        //log.info("✅ 可见人匹配成功：{} -> ID: {}", trimmedName, enterprise.getId());
                    } else {
                        log.warn("❌ 可见人未找到：{}", trimmedName);
                    }
                }
            }
            
            warehouse.setVisiblePeople(visiblePeopleIds.toString());
            warehouse.setVisiblePeopleName(visiblePeopleNamesResult.toString());
            //log.info("📝 最终可见人 ID: {}, 可见人姓名：{}", visiblePeopleIds.toString(), visiblePeopleNamesResult.toString());
        }
        
        // 填充当前登录用户的企业名称和社会信用代码
        warehouse.setEnterpriseName(currentEnterpriseName);
        warehouse.setSocialCreditCode(currentSocialCreditCode);
        
        // 状态默认为 0
        warehouse.setStatus("0");
        
        // 生成批号（与 insertBuildWarehouse 保持一致）
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = currentDate.format(formatter);
        
        // 使用内存计数器递增，避免每次都查数据库
        batchNumberCounter = batchNumberCounter + 1L;
        String countString = String.valueOf(batchNumberCounter);
        String batchNumber;
        if (batchNumberCounter < 10L) {
            batchNumber = dateString + "-0" + countString;
        } else {
            batchNumber = dateString + "-" + countString;
        }
        warehouse.setBatchNumber(batchNumber);
        
//        log.info("构建的 warehouse 对象:");
//        log.info("  - industryWarehouse: {}", warehouse.getIndustryWarehouse());
//        log.info("  - notes: {}", warehouse.getNotes());
//        log.info("  - otherDescription: {}", warehouse.getOtherDescription());
//        log.info("  - warahouseContact: {}", warehouse.getWarahouseContact());
//        log.info("  - warahousePhone: {}", warehouse.getWarahousePhone());
        
        list.add(warehouse);
        
        // 达到 BATCH_COUNT 了，需要存一次数据库，防止过多数据在内存崩溃
        if (list.size() >= BATCH_COUNT) {
            try {
                //log.info("准备批量保存 {} 条记录...", list.size());
                ecoChainBuildWarehouseService.saveBatch(list);
                numOfNew += list.size();
               // log.info("批量保存成功 {} 条记录", list.size());
                
                // 验证第一条数据是否正确保存
                EcoChainBuildWarehouse firstRecord = list.get(0);
                log.info("验证保存的数据 - ID: {}, 项目名称：{}, 经营地址：{}, 备注：{}",
                        firstRecord.getId(),
                        firstRecord.getIndustryWarehouse(),
                        firstRecord.getNotes(),
                        firstRecord.getOtherDescription());
            } catch (Exception e) {
                log.error("批量保存数据失败", e);
                throw new RuntimeException("保存数据失败：" + e.getMessage(), e);
            }
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
 /*       log.info("\n========== 导入完成统计 ==========");
        log.info("开始存储剩余数据！剩余记录数：{}", list.size());
        log.info("总共读取的行数：{} (包括最后一批)", numOfNew + list.size());*/
        if (!list.isEmpty()) {
            try {
                //log.info("准备保存最后 {} 条记录...", list.size());
                ecoChainBuildWarehouseService.saveBatch(list);
                numOfNew += list.size();
                //log.info("保存剩余 {} 条记录成功", list.size());
                
                // 验证第一条数据是否正确保存
                if (!list.isEmpty()) {
                    EcoChainBuildWarehouse firstRecord = list.get(0);
                    log.info("最终验证 - ID: {}, 项目名称：{}, 经营地址：{}, 备注：{}, 类型：{}, 所属所：{}",
                            firstRecord.getId(),
                            firstRecord.getIndustryWarehouse(),
                            firstRecord.getNotes(),
                            firstRecord.getOtherDescription(),
                            firstRecord.getDetailTypeOption(),
                            firstRecord.getIndustryGroupOption());
                }
            } catch (Exception e) {
                log.error("保存剩余数据失败", e);
                throw new RuntimeException("保存数据失败：" + e.getMessage(), e);
            }
            list.clear();
        }
        log.info("数据导入完成！共导入 {} 条记录", numOfNew);
        log.info("=====================================\n");
    }

    public Integer getNumOfNew() {
        return numOfNew;
    }
    
    /**
     * 根据所属所字段查找对应的区域配置
     * 例如：所属所为"黄岛所"，匹配 industry_group 字段包含"黄岛所"的记录
     */
    private EcoChainRegionalConfiguration findRegionalConfigByIndustryGroup(String industryGroup) {
        if (industryGroup == null || industryGroup.isEmpty()) {
            return null;
        }
        
        // 遍历所有区域配置，查找 industry_group 包含传入值的记录
        for (EcoChainRegionalConfiguration config : regionalConfigMap.values()) {
            String configIndustryGroup = config.getIndustryGroup();
            if (configIndustryGroup != null && configIndustryGroup.contains(industryGroup)) {
                return config;
            }
        }
        
        return null;
    }
}
