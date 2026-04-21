package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jdk.nashorn.internal.runtime.ErrorManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.auth.service.ISysAreaService;
import upc.c505.modular.ecochain.controller.param.*;
import upc.c505.modular.ecochain.entity.*;
import upc.c505.modular.ecochain.mapper.EcoChainProductClassificationMapper;
import upc.c505.modular.ecochain.mapper.EcoChainProductManagerMapper;
import upc.c505.modular.ecochain.service.IEcoChainProductManagerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.filemanage.utils.FileManageUtil;
import upc.c505.modular.people.service.IPeopleGovernmentService;

import java.io.File;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static upc.c505.modular.money.service.impl.MoneyProcessServiceImpl.GenerateImage;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author byh
 * @since 2024-09-25
 */
@Service
@Slf4j
public class EcoChainProductManagerServiceImpl extends ServiceImpl<EcoChainProductManagerMapper, EcoChainProductManager>
        implements IEcoChainProductManagerService {

    @Autowired
    private GetUserInfo getUserInfo;

    @Autowired
    private IEcoChainProductManagerService ecoChainProductManagerService;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private IPeopleGovernmentService peopleGovernmentService;

    @Autowired
    private EcoChainProductManagerMapper ecoChainProductManagerMapper;

    @Autowired
    private EcoChainProductClassificationMapper ecoChainProductClassificationMapper;

    private List<EcoChainProductClassification> classificationTree;

    @Override
    public boolean insertProduct(EcoChainInsertAndUpdateProductParam param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null && getUserInfo.getEnterpriseName() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            param.setEnterpriseName(getUserInfo.getEnterpriseName());
            if (ObjectUtils.isEmpty(param.getStatus())) {
                // 状态1为正常，状态0为禁用
                param.setStatus("1");
            }
            if (ObjectUtils.isEmpty(param.getTopUp()) || !Objects.equals(param.getTopUp(), "1")) {
                param.setTopUp("0");
            }
            // 存产品管理表
            if (ObjectUtils.isNotEmpty(param.getDetailedIntroduction())
                    && ObjectUtils.isNotEmpty(param.getAddressPrefix())) {
                param.setDetailedIntroduction(replaceBase64PicToUrl(param.getDetailedIntroduction(),
                        param.getSocialCreditCode(), param.getAddressPrefix()));
            }
            this.save(param);
            return true;
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }
    }

    @Override
    public Boolean updateProduct(EcoChainInsertAndUpdateProductParam param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }

        if (param.getDetailedIntroduction() != null && getUserInfo.getSocialCreditCode() != null) {
            EcoChainProductManager existingProduct = ecoChainProductManagerMapper.selectById(param.getId());
            String oldDetailedIntroduction = existingProduct.getDetailedIntroduction();

            // 替换 Base64 图片为 URL，生成最终内容
            String processedContent = param.getDetailedIntroduction();
            if (processedContent.contains("data:image")) {
                processedContent = replaceBase64PicToUrl(processedContent, getUserInfo.getSocialCreditCode(),
                        param.getAddressPrefix());
            }
            param.setDetailedIntroduction(processedContent);

            // 提取旧图片 URL 和最终内容中的 URL
            List<String> oldImageUrls = extractImageUrls(oldDetailedIntroduction);
            List<String> finalImageUrls = extractImageUrls(processedContent);

            // 计算需删除的旧图片（旧有但最终内容不再使用）
            List<String> imagesToDelete = oldImageUrls.stream()
                    .filter(url -> !finalImageUrls.contains(url))
                    .collect(Collectors.toList());

            // 删除不再使用的旧图片
            for (String imageUrl : imagesToDelete) {
                deleteImageFile(imageUrl);
            }
        }

        // 更新产品信息到数据库
        ecoChainProductManagerMapper.updateById(param);
        return true;
    }

    // 修改后的 extractImageUrls 方法
    private List<String> extractImageUrls(String detailedIntroduction) {
        List<String> imageUrls = new ArrayList<>();
        // 正则表达式：匹配包含 'upload' 的字符串及其后的路径部分
        String input = "src=\"(.*?upload.*?)(?=\")"; // 匹配 src 中有upload 的部分
        Pattern p = Pattern.compile(input);
        Matcher matcher = p.matcher(detailedIntroduction);

        while (matcher.find()) {
            // 提取匹配到的路径，并仅保留从 upload 开始的部分
            String matchedUrl = matcher.group(1);
            if (matchedUrl.contains("upload")) {
                imageUrls.add(matchedUrl.substring(matchedUrl.indexOf("upload"))); // 从 "upload" 开始的部分
            }
        }
        return imageUrls;
    }

    private void deleteImageFile(String pictureUrl) {
        try {
            // 不再拼接路径，直接使用从 src 中提取的相对路径
            String imagePath = getImageFilePath(pictureUrl);
            File imageFile = new File(imagePath);

            if (imageFile.exists() && imageFile.isFile()) {
                boolean deleted = imageFile.delete(); // 删除文件
                if (!deleted) {
                    // 如果删除失败，输出错误信息
                    System.out.println("无法删除图片文件: " + imagePath);
                }
            }
        } catch (Exception e) {
            // 捕获并打印错误信息
            e.printStackTrace();
        }
    }

    // 修改后的 getImageFilePath 方法
    private String getImageFilePath(String pictureUrl) {
        // 直接返回图片路径，不进行拼接，假设 URL 已经是完整的路径
        return "./" + pictureUrl; // 从 URL 中获取的路径即为完整路径
    }

    @Override
    public void removeProductBatch(List<Long> param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "ID 列表不能为空");
        }

        // 批量查询，减少数据库查询次数
        List<EcoChainProductManager> records = this.listByIds(param);

        // 提取所有图片 JSON
        List<String> picturesList = records.stream()
                .map(EcoChainProductManager::getPicture)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());

        // 删除图片
        FileManageUtil.handleBatchPictureDelete(picturesList);

        records.forEach(record -> {
            // 清空产品介绍字段
            record.setDetailedIntroduction(null);
            // 更新数据库中对应的记录
            this.updateById(record);
        });
        // 批量删除数据库记录
        this.removeByIds(param);

    }

    @Override
    public Page<EcoChainProductManager> selectPageProduct(EcoChainProductManagerPageSearchParam param) {
        // 一、超级管理员用户登录，查询全部
        LambdaQueryWrapper<EcoChainProductManager> queryWrapper = new LambdaQueryWrapper<>();
        if ("-1".equals(getUserInfo.getUserType())) {
            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())
                    && ObjectUtils.isNotNull(param.getSocialCreditCode())) {
                queryWrapper.eq(EcoChainProductManager::getSocialCreditCode, param.getSocialCreditCode());
            }
        }

        // 二、政府人员或二级管理员用户登录，查询管辖区域内数据
        if ("0".equals(getUserInfo.getUserType())) {
            /**
             * 管辖区域
             * 1、如果传入areaId不为空：判断传入的areaId是不是在当前用户的管辖区域内，
             * 如果传入的areaId合法，当flag为0（或者flag为空）时只查数据库中和传入areaId相等的数据，
             * 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
             * 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
             * 2、如果传入的areaId为空，那就查询该用户管辖区域的数据。
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
                        queryWrapper.eq(EcoChainProductManager::getAreaId, param.getAreaId());
                    }
                    // 当flag为1时先查当前用户的管辖区域，然后再查sys_area表里面传入areaId的子区域，
                    // 最后把两者的交集里面的areaId拿出来，到数据库里面查areaId在交集里面的数据。
                    if (ObjectUtils.isNotEmpty(param.getFlag()) && param.getFlag() == 1) {
                        List<Long> areaIdList = sysAreaService.getChildAreaIdList(param.getAreaId());
                        list.retainAll(areaIdList);
                        queryWrapper.in(EcoChainProductManager::getAreaId, list);
                    }
                } else {
                    queryWrapper.in(EcoChainProductManager::getAreaId, list);
                }
                if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())
                        && ObjectUtils.isNotNull(param.getSocialCreditCode())) {
                    queryWrapper.eq(EcoChainProductManager::getSocialCreditCode, param.getSocialCreditCode());
                }
            }
        }

        // 三、本企业人员登录，只查看本企业数据
        if ("1".equals(getUserInfo.getUserType())) {
            if (ObjectUtils.isNotEmpty(param.getSocialCreditCode())) {
                queryWrapper.eq(EcoChainProductManager::getSocialCreditCode, param.getSocialCreditCode());
            } else {
                queryWrapper
                        .eq(ObjectUtils.isNotEmpty(getUserInfo.getSocialCreditCode()),
                                EcoChainProductManager::getSocialCreditCode, getUserInfo.getSocialCreditCode());
            }
        }
        queryWrapper
                .and(ObjectUtils.isNotEmpty(param.getProductName()), w -> w
                        .like(EcoChainProductManager::getProductName, param.getProductName())
                        .or()
                        .like(EcoChainProductManager::getDetailedIntroduction, param.getProductName()))
                .eq(ObjectUtils.isNotEmpty(param.getStatus()), EcoChainProductManager::getStatus, param.getStatus())
                .eq(ObjectUtils.isNotEmpty(param.getType()), EcoChainProductManager::getType, param.getType())
                .eq(ObjectUtils.isNotEmpty(param.getTopUp()), EcoChainProductManager::getTopUp, param.getTopUp())
                .eq(ObjectUtils.isNotEmpty(param.getEcoChainProductTagId()),
                        EcoChainProductManager::getEcoChainProductTagId, param.getEcoChainProductTagId())
                .orderByDesc(EcoChainProductManager::getTopUp)
                .orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainProductManager::getOperationDatetime);

        // 统一不返回详细介绍字段
        queryWrapper.select(EcoChainProductManager.class,
                info -> !info.getColumn().equals("detailed_introduction"));

        // 如果查询参数中的产品分类id不为空
        if (ObjectUtils.isNotEmpty(param.getEcoChainProductClassificationId())) {

            List<Long> classificationIds = getAllChildClassificationIds(param.getEcoChainProductClassificationId());
            classificationIds.add(param.getEcoChainProductClassificationId());

            // 如果产品分类的id中有和上面列表中查出来的重复，则返回
            // 统一不返回详细介绍字段
            queryWrapper.select(EcoChainProductManager.class,
                    info -> !info.getColumn().equals("detailed_introduction"));
            List<EcoChainProductManager> EcoChainProductManagerList = ecoChainProductManagerMapper
                    .selectList(queryWrapper);
            List<EcoChainProductManager> emptyList = EcoChainProductManagerList.stream()
                    .filter(ecoChainProductManager -> {
                        // 获取产品分类ID字符串，如果为空则跳过
                        String productClassificationIdStr = ecoChainProductManager.getEcoChainProductClassificationId();
                        if (StringUtils.isBlank(productClassificationIdStr)) {
                            return false;
                        }

                        // 将产品分类ID字符串转换为 Long 列表
                        List<Long> productClassificationIds = Arrays.stream(productClassificationIdStr
                                .substring(1, productClassificationIdStr.length() - 1)
                                .split(","))
                                .map(String::trim)
                                .mapToLong(Long::parseLong)
                                .boxed() // 转换为 Long 对象
                                .collect(Collectors.toList());

                        // 检查产品分类ID列表是否与子分类ID列表有交集
                        return productClassificationIds.stream().anyMatch(classificationIds::contains);
                    })
                    .collect(Collectors.toList());

            // --此处需要手动设置分页
            // 计算当前页的起始索引
            int fromIndex = (int) ((param.getCurrent() - 1) * param.getSize());
            int toIndex = (int) Math.min(fromIndex + param.getSize(), emptyList.size());
            // 获取当前页的记录
            List<EcoChainProductManager> currentPageRecords = emptyList.subList(fromIndex, toIndex);
            // 创建分页对象
            Page<EcoChainProductManager> pageResult = new Page<>(param.getCurrent(), param.getSize());
            pageResult.setTotal(emptyList.size());
            pageResult.setRecords(currentPageRecords);

            return pageResult;

        } else {

            Page<EcoChainProductManager> pageInfo = new Page<>(param.getCurrent(), param.getSize());

            // 统一不返回详细介绍字段
            queryWrapper.select(EcoChainProductManager.class,
                    info -> !info.getColumn().equals("detailed_introduction"));
            this.page(pageInfo, queryWrapper);

            return pageInfo;
        }

    }

    @Override
    public List<EcoChainProductManager> selectProductByClassificationId(Long classificationId, String socialCreditCode,
            String productName, String type, Integer flag, Integer IsAsc) {
        // 1. 递归获取所有子分类ID，并将自身ID加入
        List<Long> classificationIds = getAllChildClassificationIds(classificationId);
        classificationIds.add(classificationId);

        // 2. 构建查询条件
        LambdaQueryWrapper<EcoChainProductManager> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(socialCreditCode), EcoChainProductManager::getSocialCreditCode,
                socialCreditCode);
        queryWrapper.and(ObjectUtils.isNotEmpty(productName), w -> w
                .like(EcoChainProductManager::getProductName, productName)
                .or()
                .like(EcoChainProductManager::getDetailedIntroduction, productName));
        queryWrapper.eq(ObjectUtils.isNotEmpty(type), EcoChainProductManager::getType, type);
        queryWrapper.select(EcoChainProductManager.class, info -> !info.getColumn().equals("detailed_introduction"));

        // flag==2时，添加额外条件
        if (flag == 2) {
            queryWrapper.eq(EcoChainProductManager::getStatus, 1);
            queryWrapper.orderByDesc(EcoChainProductManager::getTopUp);
            queryWrapper.orderBy(true, Objects.equals(IsAsc, 1), EcoChainProductManager::getOperationDatetime);
        }

        // 3. 查询产品列表
        List<EcoChainProductManager> ecoChainProductManagers = ecoChainProductManagerMapper.selectList(queryWrapper);

        // 4. 使用Stream API过滤，确保分类ID交集
        return ecoChainProductManagers.stream()
                .filter(ecoChainProductManager -> {
                    String productClassificationIdStr = ecoChainProductManager.getEcoChainProductClassificationId();

                    // 检查是否为空或长度过短，避免substring越界
                    if (StringUtils.isBlank(productClassificationIdStr) || productClassificationIdStr.length() <= 2) {
                        return false;
                    }

                    // 解析出ID列表
                    List<Long> productClassificationIds = Arrays.stream(
                            productClassificationIdStr.substring(1, productClassificationIdStr.length() - 1)
                                    .split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty()) // 过滤空串，避免NumberFormatException
                            .mapToLong(Long::parseLong)
                            .boxed()
                            .collect(Collectors.toList());
                    // 检查是否有交集
                    return productClassificationIds.stream().anyMatch(classificationIds::contains);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EcoChainSelectProductReturnParam> selectProduct(EcoChainSelectProductMapSearchParam param) {

        LambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(ObjectUtils.isNotEmpty(param.getSocialCreditCode()),
                        EcoChainProductClassification::getSocialCreditCode, param.getSocialCreditCode())
                .eq(EcoChainProductClassification::getClassificationGrade, 1)
                .orderByAsc(EcoChainProductClassification::getSortNumber);
        List<EcoChainProductClassification> list = ecoChainProductClassificationMapper.selectList(lambdaQueryWrapper);

        List<EcoChainSelectProductReturnParam> resultList = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(list)) {

            for (EcoChainProductClassification ecoChainProductClassification : list) {

                EcoChainSelectProductReturnParam returnParam = new EcoChainSelectProductReturnParam();

                Long id = ecoChainProductClassification.getId();
                returnParam.setId(id);
                List<EcoChainProductManager> ecoChainProductManagers = this.selectProductByClassificationId(id,
                        param.getSocialCreditCode(), param.getProductName(), param.getType(), 2, param.getIsAsc());
                if (ObjectUtils.isNotEmpty(ecoChainProductManagers)) {
                    returnParam.setChildren(ecoChainProductManagers);
                }

                if (ObjectUtils.isNotEmpty(ecoChainProductClassification.getProductClassificationName())) {
                    returnParam
                            .setProductClassificationName(ecoChainProductClassification.getProductClassificationName());
                }

                resultList.add(returnParam);

            }

        }
        return resultList;
    }

    @Override
    public Boolean updateAllProduct(EcoChainProductAllUpdate param) {
        if (ObjectUtils.isEmpty(param.getSocialCreditCode()) || ObjectUtils.isNull(param.getSocialCreditCode())) {
            throw new BusinessException(
                    BusinessErrorEnum.IS_EMPTY,
                    "社会信用代码 " + param.getSocialCreditCode() + " 不能为空");
        }
        EcoChainProductManager updateEntity = new EcoChainProductManager()
                .setContactName(param.getContactName())
                .setContactPhone(param.getContactPhone())
                .setLongitude(param.getLongitude())
                .setLatitude(param.getLatitude())
                .setAddress(param.getAddress())
                .setTagOne(param.getTagOne())
                .setTagTwo(param.getTagTwo());
        UpdateWrapper<EcoChainProductManager> wrapper = new UpdateWrapper<>();
        wrapper.eq("social_credit_code", param.getSocialCreditCode());

        return this.update(updateEntity, wrapper);
    }

    private List<Long> getAllChildClassificationIds(Long parentId) {
        List<Long> ids = new ArrayList<>();
        LambdaQueryWrapper<EcoChainProductClassification> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EcoChainProductClassification::getParentId, parentId);
        List<EcoChainProductClassification> classifications = ecoChainProductClassificationMapper
                .selectList(lambdaQueryWrapper);

        for (EcoChainProductClassification classification : classifications) {
            ids.add(classification.getId());
            // 递归调用，获取子分类的子分类ID
            ids.addAll(getAllChildClassificationIds(classification.getId()));
        }
        return ids;
    }

    private String replaceBase64PicToUrl(String content, String socialCreditCode, String addressPrefix) {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = currentDate.format(formatter);

        String path = "./upload/public/eco_chain_enterprises_storage/" + socialCreditCode + "/" + dateString + "/";
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String input = "src=\\s*\"?(.*?)(\"|>|\\s+)";
        Pattern p = Pattern.compile(input);
        Matcher matcher = p.matcher(content);
        while (matcher.find()) {
            String ret = matcher.group(1);
            if (ret.contains("data:")) {
                String filename = System.currentTimeMillis() + (int) (1 + Math.random() * 1000) + "."
                        + ret.substring(ret.indexOf("/") + 1, ret.indexOf(";"));
                GenerateImage(ret.substring(ret.indexOf(",")), path + filename);
                content = content.replace(ret, addressPrefix + "/upload/public/eco_chain_enterprises_storage/"
                        + socialCreditCode + "/" + dateString + "/" + filename);
            }
        }
        return content;
    }

}
