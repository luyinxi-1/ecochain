package upc.c505.modular.ecochain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import upc.c505.common.UserUtils;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.dict.controller.searchParam.IdParam;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.dict.entity.SysDictType;
import upc.c505.modular.dict.mapper.SysDictDataMapper;
import upc.c505.modular.dict.mapper.SysDictTypeMapper;
import upc.c505.modular.ecochain.controller.param.EcoChainDictTypePageSearchParam;
import upc.c505.modular.ecochain.controller.param.EcoChainIdParam;
import upc.c505.modular.ecochain.entity.EcoChainDictType;
import upc.c505.modular.ecochain.mapper.EcoChainDictDataMapper;
import upc.c505.modular.ecochain.mapper.EcoChainDictTypeMapper;
import upc.c505.modular.ecochain.service.IEcoChainDictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author byh
 * @since 2024-09-23
 */
@Service
public class EcoChainDictTypeServiceImpl extends ServiceImpl<EcoChainDictTypeMapper, EcoChainDictType> implements IEcoChainDictTypeService {

    @Autowired
    private EcoChainDictTypeMapper ecoChainDictTypeMapper;

    @Autowired
    private EcoChainDictDataMapper ecoChainDictDataMapper;

    @Override
    public R<PageBaseReturnParam<EcoChainDictType>> selectDictTypeList(EcoChainDictTypePageSearchParam dictType) {
        Page<EcoChainDictType> page = new Page<>(dictType.getCurrent(), dictType.getSize());
        if(ObjectUtils.isNull(dictType.getAreaIdList())){
            List<Long> areaIdList = new ArrayList<>();
            areaIdList.add(0L);
            dictType.setAreaIdList(areaIdList);
        }else{
            dictType.getAreaIdList().add(0L);
        }

        List<EcoChainDictType> list = ecoChainDictTypeMapper.selectDictTypeList(page, dictType);
        return R.page(new PageBaseReturnParam<>(page.getTotal(), page.getCurrent(), list));
    }

    @Override
    public String checkDictTypeUnique(EcoChainDictType dict) {
        Integer dictId = dict.getDictTypeId() == null ? 0 : dict.getDictTypeId();
        EcoChainDictType dictType = ecoChainDictTypeMapper.checkDictTypeUnique(dict.getDictType());
        if (dictType != null && !Objects.equals(dictType.getDictTypeId(), dictId)) {
            return "0";
        }
        return "1";
    }

    @Override
    public int insertDictType(EcoChainDictType dict) {
        if(ObjectUtils.isEmpty(dict.getCreator())){
            String name = UserUtils.get().getUsername();
            if(ObjectUtils.isNotEmpty(name)){
                dict.setCreator(name);
            }
        }
        int row = ecoChainDictTypeMapper.insertDictType(dict);
        return row;
    }

    @Override
    public int updateDictType(EcoChainDictType dict) {
        EcoChainDictType oldDict = ecoChainDictTypeMapper.selectDictTypeById(dict.getDictTypeId());
        if(ObjectUtils.isEmpty(dict.getOperator())){
            String name = UserUtils.get().getUsername();
            if(ObjectUtils.isNotEmpty(name)){
                dict.setOperator(name);
            }
        }
        if(ObjectUtils.isNotEmpty(oldDict.getCreator())){
            dict.setCreator(oldDict.getCreator());
        }
        ecoChainDictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        ecoChainDictTypeMapper.updateById(dict);
        int row = ecoChainDictTypeMapper.updateDictType(dict);
        if (row > 0) {
            List<EcoChainDictType> dictDatas = ecoChainDictDataMapper.selectDictDataByType(dict.getDictType());
        }
        return row;
    }

    @Override
    public void deleteDictTypeByIds(EcoChainIdParam idParam) {
        if (CollectionUtils.isNotEmpty(idParam.getIdList())) {
            idParam.getIdList().forEach(item -> {
                EcoChainDictType dictType = selectDictTypeById(item);
                if (dictType != null) {
                    QueryWrapper deleteWrapper = new QueryWrapper();
                    deleteWrapper.eq("dict_type", dictType.getDictType());
                    ecoChainDictDataMapper.delete(deleteWrapper);
                    ecoChainDictTypeMapper.deleteDictTypeById(item);
                } else {
                    throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "请输入正确的id");
                }
            });
        }
    }

    @Override
    public EcoChainDictType selectDictTypeById(Integer dictId) {
        return ecoChainDictTypeMapper.selectDictTypeById(dictId);
    }
}
