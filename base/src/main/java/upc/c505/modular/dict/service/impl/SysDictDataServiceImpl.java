package upc.c505.modular.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.common.wrapper.MyLambdaQueryWrapper;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;
import upc.c505.modular.dict.controller.searchParam.IdParam;
import upc.c505.modular.dict.controller.searchParam.SysDictDataSearchParam;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.dict.mapper.SysDictDataMapper;
import upc.c505.modular.dict.service.ISysDictDataService;
import upc.c505.utils.RedisUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author chenchen
 * @since 2022-06-16
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {
    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Autowired
    RedisUtils redisService;


    public String selectDictDataById(String dictTypename, Integer id) {
        QueryWrapper<SysDictData> sysDictDataQueryWrapper = new QueryWrapper<>();
        sysDictDataQueryWrapper.eq("name", dictTypename).eq("id", id);
        SysDictData sysDictData = dictDataMapper.selectOne(sysDictDataQueryWrapper);
        if (StringUtils.isNotBlank(sysDictData.getName())) {
            return sysDictData.getName();
        } else {
            return String.valueOf(id);
        }
    }

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public R<PageBaseReturnParam<SysDictData>> selectDictDataList(SysDictDataSearchParam dictData) {
        Page<SysDictData> page = new Page<>(dictData.getCurrent(), dictData.getSize());
        List<SysDictData> list = dictDataMapper.selectDictDataList(page, dictData);
        return R.page(new PageBaseReturnParam<>(page.getTotal(), page.getCurrent(), list));
    }


    /**
     * 根据字典数据类型获取字典数据
     *
     * @param dictType
     * @return
     */
    @Override
    public List<SysDictData> selectDictDataByDictType(String dictType, Long areaId, String name) {
        List<SysDictData> list = new ArrayList<>();
        list = dictDataMapper.selectByDictType(dictType, areaId, name);
        int total = list.size();
        return list;
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType 字典类型
     * @param dictKey       字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictName(String dictType, String dictKey) {
        return dictDataMapper.selectDictName(dictType, dictKey);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Integer dictCode) {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据
     *
     * @param idParam 需要删除的数据
     * @return 结果
     */
    @Override
    public void deleteDictDataByIds(IdParam idParam) {
        idParam.getIdList().forEach(item -> {
            dictDataMapper.deleteDictDataById(item);
        });
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData data) {
        MyLambdaQueryWrapper<SysDictData> lambdaQueryWrapper = new MyLambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysDictData::getAreaId, data.getAreaId())
                .eq(SysDictData::getDictKey, data.getDictKey());
        List<SysDictData> dict1 = dictDataMapper.selectList(lambdaQueryWrapper);

        if(ObjectUtils.isNotEmpty(dict1) && ObjectUtils.isNotEmpty(data.getAreaId())){
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "新增字典内容'" + data.getDictKey() + "'失败，字典内容value值已存在");
        }
        return dictDataMapper.insertDictData(data);
//        return  dictDataMapper.insert(data);
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData data) {
        return dictDataMapper.updateDictData(data);
//        return dictDataMapper.updateById(data);
    }
}
