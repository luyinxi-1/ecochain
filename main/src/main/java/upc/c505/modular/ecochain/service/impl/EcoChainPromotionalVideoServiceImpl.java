package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.controller.param.EcoChainInsertAndUpdatePromotionalVideo;
import upc.c505.modular.ecochain.controller.param.EcoChainPromotionalVideoSearchParam;
import upc.c505.modular.ecochain.entity.EcoChainPromotionalVideo;
import upc.c505.modular.ecochain.mapper.EcoChainPromotionalVideoMapper;
import upc.c505.modular.ecochain.service.IEcoChainPromotionalVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.filemanage.utils.FileManageUtil;
import upc.c505.modular.supenterprise.entity.SupEnterpriseCertification;

import java.io.File;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static upc.c505.modular.money.service.impl.MoneyProcessServiceImpl.GenerateImage;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xth
 * @since 2025-02-15
 */
@Service
public class EcoChainPromotionalVideoServiceImpl extends ServiceImpl<EcoChainPromotionalVideoMapper, EcoChainPromotionalVideo> implements IEcoChainPromotionalVideoService {

    @Autowired
    private GetUserInfo getUserInfo;

    @Override
    public void insertPromotionalVideo(EcoChainInsertAndUpdatePromotionalVideo param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "前端传参为空");
        }
        if (getUserInfo.getSocialCreditCode() != null) {
            param.setSocialCreditCode(getUserInfo.getSocialCreditCode());
            // 存储富文本数据
            if (ObjectUtils.isNotEmpty(param.getIntroduction()) && ObjectUtils.isNotEmpty(param.getAddressPrefix())) {
                param.setIntroduction(replaceBase64PicToUrl(param.getIntroduction(), param.getSocialCreditCode(), param.getAddressPrefix()));
            }

            this.save(param);
        } else {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "未查询到社会信用代码");
        }
    }

    @Override
    public Page<EcoChainPromotionalVideo> selectPromotionalVideo(EcoChainPromotionalVideoSearchParam param) {
        Page<EcoChainPromotionalVideo> page = new Page<>(param.getCurrent(), param.getSize());

        LambdaQueryWrapper<EcoChainPromotionalVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ObjectUtils.isNotNull(param.getSocialCreditCode()), EcoChainPromotionalVideo::getSocialCreditCode, param.getSocialCreditCode())
                .like(ObjectUtils.isNotNull(param.getVideoName()), EcoChainPromotionalVideo::getVideoName, param.getVideoName())
                .like(ObjectUtils.isNotNull(param.getTitle()), EcoChainPromotionalVideo::getTitle, param.getTitle())
                .orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainPromotionalVideo::getSort);

        Page<EcoChainPromotionalVideo> pageResult = this.page(page, queryWrapper);

        return pageResult;
    }

    @Override
    public void updatePromotionalVideo(EcoChainInsertAndUpdatePromotionalVideo param) {
        if (ObjectUtils.isEmpty(param)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }

        if (param.getIntroduction() != null && getUserInfo.getSocialCreditCode() != null) {
            EcoChainPromotionalVideo existingPromotionalVideo = this.getById(param.getId());
            String oldIntroduction = existingPromotionalVideo.getIntroduction();
            // 0替换 Base64 图片为 URL，生成最终内容
            String processedContent = param.getIntroduction();
            if (processedContent.contains("data:image")) {
                processedContent = replaceBase64PicToUrl(processedContent, getUserInfo.getSocialCreditCode(), param.getAddressPrefix());
            }
            param.setIntroduction(processedContent);
            // 1提取旧图片 URL 和最终内容中的 URL
            List<String> oldImageUrls = extractImageUrls(oldIntroduction);
            List<String> finalImageUrls = extractImageUrls(processedContent);
            // 2计算需删除的旧图片（旧有但最终内容不再使用）
            List<String> imagesToDelete = oldImageUrls.stream()
                    .filter(url -> !finalImageUrls.contains(url))
                    .collect(Collectors.toList());
            // 3删除不再使用的旧图片
            for (String imageUrl : imagesToDelete) {
                deleteImageFile(imageUrl);
            }

        }

        EcoChainPromotionalVideo promotionalVideo = this.getById(param.getId());
        if (!param.getVideoAddress().equals(promotionalVideo.getVideoAddress())) {
            // 删除原视频
            FileManageUtil.deleteFile(promotionalVideo.getVideoAddress());
        }
        this.updateById(param);
    }

    @Override
    public void deletePromotionalVideo(Long id) {
        if (ObjectUtils.isEmpty(id) || id == 0L) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }
        EcoChainPromotionalVideo promotionalVideo = this.getById(id);
        String oldIntroduction = promotionalVideo.getIntroduction();
        // 提取旧图片 URL
        List<String> oldImageUrls = extractImageUrls(oldIntroduction);
        // 删除不再使用的旧图片
        for (String imageUrl : oldImageUrls) {
            deleteImageFile(imageUrl);
        }

        // 删除原视频
        FileManageUtil.deleteFile(promotionalVideo.getVideoAddress());
        this.removeById(id);
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
                String filename = System.currentTimeMillis() + (int) (1 + Math.random() * 1000) + "." + ret.substring(ret.indexOf("/") + 1, ret.indexOf(";"));
                GenerateImage(ret.substring(ret.indexOf(",")), path + filename);
                content = content.replace(ret, addressPrefix + "/upload/public/eco_chain_enterprises_storage/" + socialCreditCode + "/" + dateString + "/" + filename);
            }
        }
        return content;
    }


    // 修改后的 extractImageUrls 方法
    private List<String> extractImageUrls(String detailedIntroduction) {
        List<String> imageUrls = new ArrayList<>();
        // 正则表达式：匹配包含 'upload' 的字符串及其后的路径部分
        String input = "src=\"(.*?upload.*?)(?=\")";  // 匹配 src 中有upload 的部分
        Pattern p = Pattern.compile(input);
        Matcher matcher = p.matcher(detailedIntroduction);

        while (matcher.find()) {
            // 提取匹配到的路径，并仅保留从 upload 开始的部分
            String matchedUrl = matcher.group(1);
            if (matchedUrl.contains("upload")) {
                imageUrls.add(matchedUrl.substring(matchedUrl.indexOf("upload")));  // 从 "upload" 开始的部分
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
                boolean deleted = imageFile.delete();  // 删除文件
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
        return "./" + pictureUrl;  // 从 URL 中获取的路径即为完整路径
    }

    @Override
    public EcoChainPromotionalVideo selectPromotionalVideoById(Long id) {
        if (ObjectUtils.isEmpty(id) || id == 0L) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "传参错误");
        }
        EcoChainPromotionalVideo byId = this.getById(id);
        return byId;
    }

    @Override
    public Page<EcoChainPromotionalVideo> selectPublishedPromotionalVideo(EcoChainPromotionalVideoSearchParam param) {
        Page<EcoChainPromotionalVideo> page = new Page<>(param.getCurrent(), param.getSize());

        LambdaQueryWrapper<EcoChainPromotionalVideo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ObjectUtils.isNotNull(param.getSocialCreditCode()), EcoChainPromotionalVideo::getSocialCreditCode, param.getSocialCreditCode())
                .eq(EcoChainPromotionalVideo::getIsPublish, 1)
                .like(ObjectUtils.isNotNull(param.getVideoName()), EcoChainPromotionalVideo::getVideoName, param.getVideoName())
                .like(ObjectUtils.isNotNull(param.getTitle()), EcoChainPromotionalVideo::getTitle, param.getTitle())
                .orderBy(true, Objects.equals(param.getIsAsc(), 1), EcoChainPromotionalVideo::getSort);

        Page<EcoChainPromotionalVideo> pageResult = this.page(page, queryWrapper);

        return pageResult;
    }

}
