package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.ecochain.controller.param.EcoChainDictDataPageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainIdParam;
import upc.c505.modular.ecochain.entity.EcoChainDictData;
import upc.c505.modular.ecochain.mapper.EcoChainDictDataMapper;
import upc.c505.modular.ecochain.service.IEcoChainDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import upc.c505.utils.RedisUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author byh
 * @since 2024-09-23
 */
@Service
public class EcoChainDictDataServiceImpl extends ServiceImpl<EcoChainDictDataMapper, EcoChainDictData> implements IEcoChainDictDataService {

    @Autowired
    private EcoChainDictDataMapper ecoChainDictDataMapper;

    @Autowired
    RedisUtils redisService;

    @Override
    public R<PageBaseReturnParam<EcoChainDictData>> selectDictDataList(EcoChainDictDataPageSearchParam dictData) {

        Page<EcoChainDictData> page = new Page<>(dictData.getCurrent(), dictData.getSize());
        List<EcoChainDictData> list = ecoChainDictDataMapper.selectDictDataList(page, dictData);

        return R.page(new PageBaseReturnParam<>(page.getTotal(), page.getCurrent(), list));
    }

    @Override
    public int insertDictData(EcoChainDictData dict) {

        // 当参数中areaId相同且dictKey也相同的情况下才添加失败，如果两个areaId都是空且dictKey相同的话也是添加失败的
        MyLambdaQueryWrapper<EcoChainDictData> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EcoChainDictData::getAreaId, dict.getAreaId())
                .eq(EcoChainDictData::getDictKey, dict.getDictKey());
        List<EcoChainDictData> dict1 = ecoChainDictDataMapper.selectList(lambdaQueryWrapper);

        if(ObjectUtils.isNotEmpty(dict1) && ObjectUtils.isNotEmpty(dict.getAreaId())){
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "新增字典内容'" + dict.getDictKey() + "'失败，字典内容value值已存在");
        }

        //没有自动注入创建人信息，手动获取一下
        if(ObjectUtils.isEmpty(dict.getCreator())){
            dict.setCreator(UserUtils.get().getUsername());
        }

        return ecoChainDictDataMapper.insertDictData(dict);
    }

    @Override
    public int updateDictData(EcoChainDictData dict) {

        // 获取当前操作人的名称
        if(ObjectUtils.isEmpty(dict.getOperator())){
            dict.setOperator(UserUtils.get().getUsername());
        }

        // 更新的时候如果没有传状态参数，注意保存原来的
        if(ObjectUtils.isEmpty(dict.getStatus())){
            EcoChainDictData dict1 = ecoChainDictDataMapper.selectById(dict.getId());
            if(ObjectUtils.isNotEmpty(dict1) && ObjectUtils.isNotEmpty(dict1.getStatus())){
                dict.setStatus(dict1.getStatus());
            }
        }

        return ecoChainDictDataMapper.updateDictData(dict);
    }

    @Override
    public void deleteDictDataByIds(EcoChainIdParam idParam) {
        idParam.getIdList().forEach(item -> {
            ecoChainDictDataMapper.deleteDictDataById(item);
        });
    }

    @Override
    public List<EcoChainDictData> selectDictDataByDictType(String dictType, Long areaId, String name) {

        List<EcoChainDictData> list = new ArrayList<>();
        list = ecoChainDictDataMapper.selectByDictType(dictType, areaId, name);
        int total = list.size();

        return list;
    }

    @Override
    public String selectDictName(String dictType, String dictKey) {

        return ecoChainDictDataMapper.selectDictName(dictType, dictKey);

    }
}
