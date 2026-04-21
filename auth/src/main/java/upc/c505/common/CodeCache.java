package upc.c505.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import upc.c505.modular.auth.entity.SysArea;
import upc.c505.modular.auth.mapper.NewSysAreaMapper;
import upc.c505.modular.dict.entity.SysDictData;
import upc.c505.modular.dict.mapper.SysDictDataMapper;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CodeCache {
    public static Map<String,String> codeNameMap = new HashMap<String, String>();
    public static Map<String, String> areaMap = new HashMap<String, String>();
    public static Map<String, String> cascadeMap = new HashMap<String, String>();

    @Autowired
    private NewSysAreaMapper sysAreaMapper;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;
//    @Autowired
//    private CascadeConfigMapper cascadeConfigMapper;

    @PostConstruct
    public void init() {
        System.out.println("刷新区域缓存");
        List<SysArea> sysAreaList = sysAreaMapper.selectList(new QueryWrapper<>());
        for (SysArea sysArea : sysAreaList) {
            areaMap.put("Area"+sysArea.getId(), sysArea.getAreaName());
        }
        System.out.println("刷新字典缓存");
        List<SysDictData> sysDictDataList = sysDictDataMapper.selectList(new QueryWrapper<>());
        for (SysDictData sysDictData : sysDictDataList) {
            codeNameMap.put(sysDictData.getDictType() + sysDictData.getId(), sysDictData.getName());
        }
        System.out.println("刷新级联菜单缓存");
//        List<CascadeConfig> cascadeConfigList = cascadeConfigMapper.selectList(new QueryWrapper<>());
//        for (CascadeConfig cascadeConfig : cascadeConfigList) {
//            cascadeMap.put( "Cascade" + cascadeConfig.getId(), cascadeConfig.getName());
//        }
    }

    public static String transformDictName(String dictTypename, Integer value){
        return codeNameMap.get(dictTypename+ value);
    }

    public static String transformAreaName(Integer value) {

        return areaMap.get("Area"+ value);
    }

    public static String transformCascadeName(Integer value) {
        return cascadeMap.get("Cascade"+ value);
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
    }


    @Scheduled(cron = "0 0 0/2 * * ?")
    public void testOne() {
        //每2小时执行一次缓存
        init();
    }

}