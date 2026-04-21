package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAuthorizePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainEnterpriseAuthorizeReturnParam;
import upc.c505.modular.ecochain.entity.EcoChainEnterpriseAuthorize;
import upc.c505.modular.ecochain.mapper.EcoChainEnterpriseAuthorizeMapper;
import upc.c505.modular.ecochain.service.IEcoChainEnterpriseAuthorizeService;
import upc.c505.modular.supenterprise.entity.SupEnterprise;
import upc.c505.modular.supenterprise.mapper.SupEnterpriseMapper;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mjh
 * @since 2024-09-25
 */
@Service
public class EcoChainEnterpriseAuthorizeServiceImpl extends ServiceImpl<EcoChainEnterpriseAuthorizeMapper, EcoChainEnterpriseAuthorize> implements IEcoChainEnterpriseAuthorizeService {

    @Autowired
    private EcoChainEnterpriseAuthorizeMapper ecoChainEnterpriseAuthorizeMapper;

    @Autowired
    private SupEnterpriseMapper supEnterpriseMapper;

    @Override
    public EcoChainEnterpriseAuthorizeReturnParam selectById(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessException(BusinessErrorEnum.IS_EMPTY, "企业不存在");
        }
        EcoChainEnterpriseAuthorize ecoChainEnterpriseAuthorize = this.getById(id);
        if(ObjectUtils.isNotEmpty(ecoChainEnterpriseAuthorize)) {
            EcoChainEnterpriseAuthorizeReturnParam returnParam = new EcoChainEnterpriseAuthorizeReturnParam();
            BeanUtils.copyProperties(ecoChainEnterpriseAuthorize, returnParam);

            constructReturnParam(returnParam);
            return returnParam;
        }
        return null;
    }

    @Override
    public Page<EcoChainEnterpriseAuthorizeReturnParam> selectPage(EcoChainEnterpriseAuthorizePageSearchParam param) {
        Page<EcoChainEnterpriseAuthorizeReturnParam> returnParamPage = new Page<>();

        MyLambdaQueryWrapper<EcoChainEnterpriseAuthorize> queryWrapper= new MyLambdaQueryWrapper<>();
        queryWrapper.eq(EcoChainEnterpriseAuthorize::getAuthorizeStatus, param.getAuthorizeStatus());

        // 模糊查询企业名称
        if(ObjectUtils.isNotEmpty(param.getSupEnterpriseName())){
            LambdaQueryWrapper<SupEnterprise> supEnterpriseQueryWrapper = new LambdaQueryWrapper<>();
            supEnterpriseQueryWrapper.like(SupEnterprise::getSupEnterpriseName, param.getSupEnterpriseName())
                    .select(SupEnterprise::getSocialCreditCode);
            List<String> likeQuerySocialCreditCodeList = supEnterpriseMapper.selectList(supEnterpriseQueryWrapper)
                    .stream().map(SupEnterprise::getSocialCreditCode).collect(Collectors.toList());

            //      模糊查询到的企业为空的话，直接返回
            if(!ObjectUtils.isNotEmpty(likeQuerySocialCreditCodeList)){
                return returnParamPage;
            }
            // 模糊查询不为空，添加条件
            else {
                queryWrapper.in(EcoChainEnterpriseAuthorize::getCreditCode, likeQuerySocialCreditCodeList);
                queryWrapper.eq(EcoChainEnterpriseAuthorize::getCreditCode, param.getCreditCode())
                        .eq(EcoChainEnterpriseAuthorize::getManagerName, param.getManagerName())
                        .eq(EcoChainEnterpriseAuthorize::getManagerPhone, param.getManagerPhone())
                        .orderByDesc(EcoChainEnterpriseAuthorize::getOperationDatetime);
            }
        }
        else {
            queryWrapper.eq(EcoChainEnterpriseAuthorize::getCreditCode, param.getCreditCode())
                    .eq(EcoChainEnterpriseAuthorize::getManagerName, param.getManagerName())
                    .eq(EcoChainEnterpriseAuthorize::getManagerPhone, param.getManagerPhone())
                    .orderByDesc(EcoChainEnterpriseAuthorize::getOperationDatetime);
        }

        Page<EcoChainEnterpriseAuthorize> ecoChainEnterpriseAuthorizePage = ecoChainEnterpriseAuthorizeMapper.selectPage(
                new Page<>(param.getCurrent(), param.getSize()), queryWrapper);

//      ---------------复制结果到返回类中 begin---------------
        List<EcoChainEnterpriseAuthorize> records = ecoChainEnterpriseAuthorizePage.getRecords();
        List<EcoChainEnterpriseAuthorizeReturnParam> tempList = new ArrayList<>();
        for (EcoChainEnterpriseAuthorize item: records) {
            EcoChainEnterpriseAuthorizeReturnParam temp = new EcoChainEnterpriseAuthorizeReturnParam();
            BeanUtils.copyProperties(item, temp);
            tempList.add(temp);
        }

        returnParamPage.setRecords(tempList);
        returnParamPage.setCurrent(ecoChainEnterpriseAuthorizePage.getCurrent());
        returnParamPage.setPages(ecoChainEnterpriseAuthorizePage.getPages());
        returnParamPage.setSize(ecoChainEnterpriseAuthorizePage.getSize());
        returnParamPage.setTotal(ecoChainEnterpriseAuthorizePage.getTotal());
//      ---------------复制结果到返回类中 end---------------

        // 构造新数据:EcoChainEnterpriseAuthorize中没有的数据，但返回类中需要
        for (EcoChainEnterpriseAuthorizeReturnParam item: returnParamPage.getRecords()) {
            constructReturnParam(item);
        }
        return returnParamPage;
    }

    @Override
    public Boolean updateAuthorize(EcoChainEnterpriseAuthorize param) {
        int dataBaseSatus = this.getById(param.getId()).getAuthorizeStatus();
        int newStatus = param.getAuthorizeStatus();
        // 授权状态未改变
        if(dataBaseSatus == newStatus) return this.updateById(param);

// -------------- 授权状态改变 --------------
        // 未授权 -> 正常/试用
        if(dataBaseSatus == 0 && newStatus != 0){
            // 创建目录
            File directory = new File("upload/public/eco_chain_enterprises_storage/"+param.getCreditCode());
            directory.mkdir();
            return this.updateById(param);
        }
        // 正常/试用 -> 未授权
        else if(newStatus == 0 && dataBaseSatus != 0){
            return this.updateById(param);
        }
        // 试用 -> 正常
        else if(dataBaseSatus == 2 && newStatus == 1) {
            return this.updateById(param);
        }

        return this.updateById(param);
    }

    @Override
    public String removeBySocialCreditCodeBatch(List<String> socialCreditCodeList) {
        List<EcoChainEnterpriseAuthorize> authorizeList = ecoChainEnterpriseAuthorizeMapper.selectList(new LambdaQueryWrapper<EcoChainEnterpriseAuthorize>()
                .in(EcoChainEnterpriseAuthorize::getCreditCode, socialCreditCodeList));
        // 获取所有需要删除的授权记录的社会信用代码列表
        List<String> creditCodesInAuthorizeList = authorizeList.stream()
                .map(EcoChainEnterpriseAuthorize::getCreditCode)
                .collect(Collectors.toList());
        // 获取 socialCreditCodeList 中不在 authorizeList 中的社会信用代码
        List<String> notFoundCreditCodes = socialCreditCodeList.stream()
                .filter(code -> !creditCodesInAuthorizeList.contains(code))
                .collect(Collectors.toList());
        this.removeBatchByIds(authorizeList);
        String returnMsg = "";
        if(!ObjectUtils.isNotEmpty(notFoundCreditCodes)){
            returnMsg = "成功:" + authorizeList.size();
        }
        else returnMsg = "成功-" + authorizeList.size() + ",失败-" + notFoundCreditCodes.size() + ":" + notFoundCreditCodes.toString();
        return returnMsg;
    }

    @Override
    public void constructReturnParam(EcoChainEnterpriseAuthorizeReturnParam returnParam) {
        SupEnterprise supEnterprise = supEnterpriseMapper.selectOne(
                new LambdaQueryWrapper<SupEnterprise>().eq(
                        SupEnterprise::getSocialCreditCode, returnParam.getCreditCode())
        );
        if(ObjectUtils.isNotEmpty(supEnterprise)) {
            if(ObjectUtils.isNotEmpty(supEnterprise.getSupEnterpriseName()))            // 企业名称
                returnParam.setSupEnterpriseName(supEnterprise.getSupEnterpriseName());
            if(ObjectUtils.isNotEmpty(supEnterprise.getSupEnterpriseName()))            // 联络人
                returnParam.setContactName(supEnterprise.getContactName());
            if(ObjectUtils.isNotEmpty(supEnterprise.getSupEnterpriseName()))            // 联络人电话
                returnParam.setContactPhone(supEnterprise.getContactPhone());
        }
//        设置使用大小
        returnParam.setUsedSize(this.queryUsedSize(returnParam.getCreditCode()));

        if(ObjectUtils.isNotEmpty(returnParam.getStorageCapacity()) && ObjectUtils.isNotEmpty(returnParam.getEndDate())){
//          设置用户状态 : 0正常、1容量报警、2授权预警、3已超期、4容量报警&授权预警、5容量报警&已超期
            int capacityStatus = -1; // 0正常、1容量报警
            int authorizeStatus = -1; // 1正常、2授权预警、3已超期

            if(returnParam.getUsedSize() >= returnParam.getStorageCapacity()) capacityStatus = 1;
            else capacityStatus = 0;

            if(ChronoUnit.DAYS.between(LocalDateTime.now(), returnParam.getEndDate()) < 0) authorizeStatus = 3;
            else if(ChronoUnit.DAYS.between(LocalDateTime.now(), returnParam.getEndDate()) < 7) authorizeStatus = 2;
            else authorizeStatus = 1;

            if (capacityStatus == 0 && authorizeStatus == 1) returnParam.setUserStatus(0);      // 0正常
            else if (capacityStatus == 1 && authorizeStatus == 1) returnParam.setUserStatus(1); // 1容量报警
            else if (capacityStatus == 0 && authorizeStatus == 2) returnParam.setUserStatus(2); // 2授权预警
            else if (capacityStatus == 0 && authorizeStatus == 3) returnParam.setUserStatus(3); // 3已超期
            else if (capacityStatus == 1 && authorizeStatus == 2) returnParam.setUserStatus(4); // 4容量报警&授权预警
            else if (capacityStatus == 1 && authorizeStatus == 3) returnParam.setUserStatus(5); // 5容量报警&已超期
        }

    }

    @Override
    public Double queryUsedSize(String creditCode) {
        File folder = new File("upload/public/eco_chain_enterprises_storage/"+creditCode);

        if (folder.exists() && folder.isDirectory()) {
            long size = FileUtils.sizeOfDirectory(folder);  // 计算文件夹大小
            double res = Math.round((double)size / (1024 * 1024 * 1024) * 100.0) / 100.0;   // 单位换算 Byte->G 保留两位小数
            return res;
        } else {
            return 0.00;
        }
    }

    public Integer getUserStatus(Double storageCapacity, LocalDateTime endDate, Double userSize){
        if(ObjectUtils.isNotEmpty(storageCapacity) && ObjectUtils.isNotEmpty(endDate)){
//          设置用户状态 : 0正常、1容量报警、2授权预警、3已超期、4容量报警&授权预警、5容量报警&已超期
            int capacityStatus = -1; // 0正常、1容量报警
            int authorizeStatus = -1; // 1正常、2授权预警、3已超期

            if(userSize >= storageCapacity) capacityStatus = 1;
            else capacityStatus = 0;

            if(ChronoUnit.DAYS.between(LocalDateTime.now(), endDate) < 0) authorizeStatus = 3;
            else if(ChronoUnit.DAYS.between(LocalDateTime.now(), endDate) < 7) authorizeStatus = 2;
            else authorizeStatus = 1;

            if (capacityStatus == 0 && authorizeStatus == 1) return 0;      // 0正常
            else if (capacityStatus == 1 && authorizeStatus == 1) return 1; // 1容量报警
            else if (capacityStatus == 0 && authorizeStatus == 2) return 2; // 2授权预警
            else if (capacityStatus == 0 && authorizeStatus == 3) return 3; // 3已超期
            else if (capacityStatus == 1 && authorizeStatus == 2) return 4; // 4容量报警&授权预警
            else if (capacityStatus == 1 && authorizeStatus == 3) return 5; // 5容量报警&已超期
        }
        return null;
    }
}
