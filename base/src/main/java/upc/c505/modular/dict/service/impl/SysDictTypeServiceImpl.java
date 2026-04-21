package upc.c505.modular.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.dict.controller.searchParam.IdParam;
import upc.c505.modular.dict.controller.searchParam.SysDictTypeSearchParam;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.dict.entity.SysDictType;
import upc.c505.modular.dict.mapper.SysDictDataMapper;
import upc.c505.modular.dict.mapper.SysDictTypeMapper;
import upc.c505.modular.dict.service.ISysDictTypeService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author chenchen
 * @since 2022-06-16
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {
    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;


    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public R<PageBaseReturnParam<SysDictType>> selectDictTypeList(SysDictTypeSearchParam dictType) {
        Page<SysDictType> page = new Page<>(dictType.getCurrent(), dictType.getSize());
        if(ObjectUtils.isNull(dictType.getAreaIdList())){
            List<Long> areaIdList = new ArrayList<>();
            areaIdList.add(0L);
            dictType.setAreaIdList(areaIdList);
        }else{
            dictType.getAreaIdList().add(0L);
        }

        List<SysDictType> list = dictTypeMapper.selectDictTypeList(page, dictType);
        return R.page(new PageBaseReturnParam<>(page.getTotal(), page.getCurrent(), list));
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return dictTypeMapper.selectDictTypeAll();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dictType);
        if (CollectionUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Integer dictId) {
        return dictTypeMapper.selectDictTypeById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        return dictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     * 批量删除字典类型
     *
     * @param idParam 需要删除的数据
     * @return 结果
     */
    @Override
    public void deleteDictTypeByIds(IdParam idParam) {
        if (CollectionUtils.isNotEmpty(idParam.getIdList())) {
            idParam.getIdList().forEach(item -> {
                SysDictType dictType = selectDictTypeById(item);
                if (dictType != null) {
                    QueryWrapper deleteWrapper = new QueryWrapper();
                    deleteWrapper.eq("dict_type", dictType.getDictType());
                    dictDataMapper.delete(deleteWrapper);
                    dictTypeMapper.deleteDictTypeById(item);
                } else {
                    throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "请输入正确的id");
                }
            });
        }
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(SysDictType dict) {
        int row = dictTypeMapper.insertDictType(dict);
        return row;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictType(SysDictType dict) {
        SysDictType oldDict = dictTypeMapper.selectDictTypeById(dict.getDictTypeId());
        dictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        int row = dictTypeMapper.updateDictType(dict);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dict.getDictType());
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(SysDictType dict) {
        Integer dictId = dict.getDictTypeId() == null ? 0 : dict.getDictTypeId();
        SysDictType dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictType());
        if (dictType != null && dictType.getDictTypeId() != dictId) {
            return "0";
        }
        return "1";
    }
}
