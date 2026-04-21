package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.FieldDocument;
import upc.c505.common.responseparam.R;
import upc.c505.modular.ecochain.controller.param.EcoChainExtendedTableUpdateParam;
import upc.c505.modular.ecochain.entity.EcoChainExtendedTable;
import upc.c505.modular.ecochain.mapper.EcoChainExtendedTableMapper;
import upc.c505.modular.ecochain.service.IEcoChainExtendedTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import upc.c505.modular.ecochain.util.GetUserInfo;
import upc.c505.modular.govservice.entity.GovServiceWorkState;
import upc.c505.modular.partybuilding.entity.PartyBuildingProbationaryPartyMemberInformation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static upc.c505.modular.filemanage.utils.FileManageUtil.deleteFile;
import static upc.c505.modular.money.service.impl.MoneyProcessServiceImpl.GenerateImage;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author la
 * @since 2024-09-24
 */
@Service
public class EcoChainExtendedTableServiceImpl extends ServiceImpl<EcoChainExtendedTableMapper, EcoChainExtendedTable> implements IEcoChainExtendedTableService {

    @Autowired
    EcoChainExtendedTableMapper ecoChainExtendedTableMapper;

    @Autowired
    GetUserInfo getUserInfo;
    @Override
    public Integer insertExtendedTable(EcoChainExtendedTableUpdateParam ecoChainExtendedTable) {
        ecoChainExtendedTable.setCompanyIntroduction(replaceBase64PicToUrl(ecoChainExtendedTable.getCompanyIntroduction(), getUserInfo.getSocialCreditCode(), ecoChainExtendedTable.getAddressPrefix()));
        return ecoChainExtendedTableMapper.insert(ecoChainExtendedTable);
    }

    @Override
    public Integer updateExtendedTable(EcoChainExtendedTableUpdateParam param) {
        EcoChainExtendedTable existingRecord = ecoChainExtendedTableMapper.selectById(param.getId());

        if (existingRecord == null) {
            throw new RuntimeException("Record not found");
        }

        if (param.getCompanyIntroduction() !=null ) {
            String newIntroValue = param.getCompanyIntroduction();
            String oldIntroValue = existingRecord.getCompanyIntroduction();
            
            // 如果新值与旧值不同 (即使新值是空字符串""，也认为不同于一个非空旧值)
            if (!StringUtils.equals(newIntroValue, oldIntroValue)) {
                // 如果旧的介绍存在，则删除旧文件
                if (ObjectUtils.isNotNull(existingRecord.getCompanyIntroduction()) && ObjectUtils.isNotEmpty(existingRecord.getCompanyIntroduction())) {
                    deleteFile(replaceBase64PicToUrl(oldIntroValue,
                            getUserInfo.getSocialCreditCode(), param.getAddressPrefix()));
                }
                if(ObjectUtils.isEmpty(param.getCompanyIntroduction())){
                    param.setCompanyIntroduction("");
                }else{
                    param.setCompanyIntroduction(replaceBase64PicToUrl(newIntroValue,
                            getUserInfo.getSocialCreditCode(), param.getAddressPrefix()));
                }
            }
        } else {
            // 客户端未传递 companyIntroduction 字段，保留数据库中的旧值
            String oldIntroValue = existingRecord.getCompanyIntroduction();
            if (StringUtils.isNotBlank(oldIntroValue)) {
                param.setCompanyIntroduction(oldIntroValue);
            }
        }

        if (param.getDarkLogo() != null) { // 客户端传递了 darkLogo
            if (!StringUtils.equals(param.getDarkLogo(), existingRecord.getDarkLogo())) {
                if (StringUtils.isNotBlank(existingRecord.getDarkLogo())) {
                    deleteFile(existingRecord.getDarkLogo());
                }
            }
        } else {
            // 客户端未传递 darkLogo，保留旧值
            String oldDark = existingRecord.getDarkLogo();
            if (StringUtils.isNotBlank(oldDark)) {
                param.setDarkLogo(oldDark);
            }
        }

        if (param.getLightColorLogo() != null) {
            if (!StringUtils.equals(param.getLightColorLogo(), existingRecord.getLightColorLogo())) {
                if (StringUtils.isNotBlank(existingRecord.getLightColorLogo())) {
                    deleteFile(existingRecord.getLightColorLogo());
                }
            }
        } else {
            String oldLight = existingRecord.getLightColorLogo();
            if (StringUtils.isNotBlank(oldLight)) {
                param.setLightColorLogo(oldLight);
            }
        }

        if (param.getWechatQrCode() != null) {
            if (!StringUtils.equals(param.getWechatQrCode(), existingRecord.getWechatQrCode())) {
                if (StringUtils.isNotBlank(existingRecord.getWechatQrCode())) {
                    deleteFile(existingRecord.getWechatQrCode());
                }
            }
        } else {
            String oldQr = existingRecord.getWechatQrCode();
            if (StringUtils.isNotBlank(oldQr)) {
                param.setWechatQrCode(oldQr);
            }
        }

        if (param.getBackgroundPicture() != null) {
            if (!StringUtils.equals(param.getBackgroundPicture(), existingRecord.getBackgroundPicture())) {
                if (StringUtils.isNotBlank(existingRecord.getBackgroundPicture())) {
                    deleteFile(existingRecord.getBackgroundPicture());
                }
            }
        } else {
            String oldBp = existingRecord.getBackgroundPicture();
            if (StringUtils.isNotBlank(oldBp)) {
                param.setBackgroundPicture(oldBp);
            }
        }

        if (param.getSocialCreditCode() == null) {
            param.setSocialCreditCode(existingRecord.getSocialCreditCode());
        }
        if (param.getMerchantPromises() == null) {
            param.setMerchantPromises(existingRecord.getMerchantPromises());
        }
        if (param.getPhoneNumberOne() == null) {
            param.setPhoneNumberOne(existingRecord.getPhoneNumberOne());
        }
        if (param.getPhoneNumberTwo() == null) {
            param.setPhoneNumberTwo(existingRecord.getPhoneNumberTwo());
        }
        if (param.getWelfareSlogan() == null) {
            param.setWelfareSlogan(existingRecord.getWelfareSlogan());
        }
        if (param.getColorVersion() == null) {
            param.setColorVersion(existingRecord.getColorVersion());
        }
        if (param.getVrExhibition() == null) {
            param.setVrExhibition(existingRecord.getVrExhibition());
        }
        if (param.getMiniProgramHomepageConfiguration() == null) {
            param.setMiniProgramHomepageConfiguration(existingRecord.getMiniProgramHomepageConfiguration());
        }
        if (param.getMiniProgramShareDisplayConfiguration() == null) {
            param.setMiniProgramShareDisplayConfiguration(existingRecord.getMiniProgramShareDisplayConfiguration());
        }
        if (param.getMiniProgramShareHomepageConfiguration() == null) {
            param.setMiniProgramShareHomepageConfiguration(existingRecord.getMiniProgramShareHomepageConfiguration());
        }
        if (param.getMessageAuthorizationNotification() == null) {
            param.setMessageAuthorizationNotification(existingRecord.getMessageAuthorizationNotification());
        }
        if (param.getDisplayHeat() == null) {
            param.setDisplayHeat(existingRecord.getDisplayHeat());
        }
        if (param.getDisplayBrandStory() == null) {
            param.setDisplayBrandStory(existingRecord.getDisplayBrandStory());
        }
        if (param.getBasicInfo() == null) {
            param.setBasicInfo(existingRecord.getBasicInfo());
        }
        if (param.getShareDisplayTitle() == null) {
            param.setShareDisplayTitle(existingRecord.getShareDisplayTitle());
        }

        return ecoChainExtendedTableMapper.updateById(param);
    }

    @Override
    public EcoChainExtendedTable selectExtendedTable(EcoChainExtendedTable param) {
        EcoChainExtendedTable ecoChainExtendedTable = ecoChainExtendedTableMapper.selectOne(
                new LambdaQueryWrapper<EcoChainExtendedTable>()
                        .eq(EcoChainExtendedTable::getSocialCreditCode,param.getSocialCreditCode())
        );
        if(ObjectUtils.isEmpty(ecoChainExtendedTable)||ObjectUtils.isNull(ecoChainExtendedTable)){
            ecoChainExtendedTableMapper.insert(param);
            return param;
        }
        return ecoChainExtendedTable;
    }

    private String replaceBase64PicToUrl(String content, String socialCreditCode, String addressPrefix) {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = currentDate.format(formatter);

        String path = "./upload/public/eco_chain_extended_table/" + socialCreditCode + "/" + dateString + "/";
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
                content = content.replace(ret, addressPrefix + "/upload/public/eco_chain_extended_table/" + socialCreditCode + "/" + dateString + "/" + filename);
            }
        }
        return content;
    }


}
