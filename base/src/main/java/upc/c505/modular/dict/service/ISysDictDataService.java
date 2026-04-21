package upc.c505.modular.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import upc.c505.common.responseparam.PageBaseReturnParam;
import upc.c505.common.responseparam.R;
import upc.c505.modular.dict.controller.searchParam.IdParam;
import upc.c505.modular.dict.controller.searchParam.SysDictDataSearchParam;
import upc.c505.modular.dict.entity.SysDictData;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author chenchen
 * @since 2022-06-16
 */
public interface ISysDictDataService extends IService<SysDictData> {


    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public R<PageBaseReturnParam<SysDictData>> selectDictDataList(SysDictDataSearchParam dictData);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType 字典类型
     * @return 字典标签
     */
    public String selectDictName(String dictType, String dictKey);

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    public SysDictData selectDictDataById(Integer dictCode);

    /**
     * 批量删除字典数据
     *
     * @param idParam 需要删除的数据
     * @return 结果
     */
    public void deleteDictDataByIds(IdParam idParam);

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int updateDictData(SysDictData dictData);

    //根据字典数据类型获取字典数据
    List<SysDictData> selectDictDataByDictType(String dictType, Long areaId, String name);
}
