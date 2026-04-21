package upc.c505.modular.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.dict.controller.searchParam.IdParam;
import upc.c505.modular.dict.controller.searchParam.SysDictTypeSearchParam;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.dict.entity.SysDictType;

import java.util.List;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author chenchen
 * @since 2022-06-16
 */
public interface ISysDictTypeService extends IService<SysDictType> {
    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    public R<PageBaseReturnParam<SysDictType>> selectDictTypeList(SysDictTypeSearchParam dictType);

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    public SysDictType selectDictTypeById(Integer dictId);

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     * 批量删除字典类型
     *
     * @param idParam 需要删除的数据
     * @return 结果
     * @throws Exception 异常
     */
    public void deleteDictTypeByIds(IdParam idParam);

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int insertDictType(SysDictType dictType);

    public int updateDictType(SysDictType dict);

    public String checkDictTypeUnique(SysDictType dict);
}
