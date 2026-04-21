package upc.c505.modular.villageiot.util;

import upc.c505.exception.BusinessException;
import upc.c505.modular.employeetrain.exception.EmBusinessError;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import static upc.c505.modular.villageiot.controller.param.VideoConst.*;

/**
 * @author: frd
 * @create-date: 2024/7/8 10:18
 */

public class VideoUtil {
    //企业许可类型
    public static final String TYPE_NAME = "typeName";
    //企业许可表名
    public static final String TABLE_NAME = "tableName";
    //    编号
    public static final String TABLE_ID = "tableId";

    public static final String PARENT_CLASS = "parentClass";

    public static final String SUB_CLASS = " subclass";

    public static void toAIforInspection(String folders) {
        try {
            Socket socket = new Socket("27.223.88.150", 9091);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(folders);
            writer.flush();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将编号转化为表名
     *
     * @param typeId 编号
     * @return 表名及许可类型
     */
    public static Map<String, String> getTableName(Integer typeId) {
        Map<String, String> map = new HashMap<>();
        switch (typeId) {
            case 2:
                //食品经营
            {
                map.put(TYPE_NAME, "食品经营");
                map.put(TABLE_NAME, FOOD_BUSINESS);
                map.put(TABLE_ID, String.valueOf(2));
                return map;
            }
            case 3:
                //食品生产
            {
                map.put(TYPE_NAME, "食品生产");
                map.put(TABLE_NAME, FOOD_PRODUCE);
                map.put(TABLE_ID, String.valueOf(3));
                return map;
            }
            case 4:
                //小餐饮
            {
                map.put(TYPE_NAME, "小餐饮");
                map.put(TABLE_NAME, SMALL_CATER);
                map.put(TABLE_ID, String.valueOf(4));
                return map;
            }
            case 5:
                //小作坊
            {
                map.put(TYPE_NAME, "小作坊");
                map.put(TABLE_NAME, SMALL_WORKSHOP);
                map.put(TABLE_ID, String.valueOf(5));
                return map;
            }
            case 6:
                //药品经营
            {
                map.put(TYPE_NAME, "药品经营");
                map.put(TABLE_NAME, DRUG_BUSINESS);
                map.put(TABLE_ID, String.valueOf(6));
                return map;
            }
            case 7:
                //医疗器械经营
            {
                map.put(TYPE_NAME, "医疗器械经营");
                map.put(TABLE_NAME, MEDICAL_BUSINESS);
                map.put(TABLE_ID, String.valueOf(7));
                return map;
            }
            case 8:
                //药品生产
            {
                map.put(TYPE_NAME, "药品生产");
                map.put(TABLE_NAME, DRUG_PRODUCE);
                map.put(TABLE_ID, String.valueOf(8));
                return map;
            }
            case 9:
                //医疗器械生产
            {
                map.put(TYPE_NAME, "医疗器械生产");
                map.put(TABLE_NAME, MEDICAL_PRODUCE);
                map.put(TABLE_ID, String.valueOf(9));
                return map;
            }
            case 10:
                //化妆品生产
            {
                map.put(TYPE_NAME, "化妆品生产");
                map.put(TABLE_NAME, COSMETICS_PRODUCE);
                map.put(TABLE_ID, String.valueOf(10));
                return map;
            }
            case 11:
                //工业产品生产
            {
                map.put(TYPE_NAME, "工业产品生产");
                map.put(TABLE_NAME, INDUSTRIAL_PRODUCE);
                map.put(TABLE_ID, String.valueOf(11));
                return map;
            }
            case 12:
                return map;
            case 13:
                //设备维保单位
            {
                map.put(TYPE_NAME, "设备维保单位");
                map.put(TABLE_NAME, MAINTENANCE);
                map.put(TABLE_ID, String.valueOf(13));
                return map;
            }
            default:
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "参数错误请刷新后重试");
        }
    }

    /**
     * 将列表的列表展开成一个set(此方法只适用于特定格式，不针对所有情况，需要此功能请单独写一个或者使用其他工具类)
     *
     * @return Map<String, Set < String>,key是类别
     */
    public static Map<String, Set<String>> unfoldList(List<List> lists) {
        if (lists == null) {
            return Collections.emptyMap();
        }
        HashMap<String, Set<String>> map = new HashMap<>();
//        存放大类
        map.put(PARENT_CLASS, new HashSet<>());
        map.put(SUB_CLASS, new HashSet<>());
        System.out.println("the videoUtil lists size is: " + lists.size());
        lists.forEach(item -> {
            if (item.size() == 0) {
                return;
            } else if (item.size() == 1) {
//                如果只有一个元素，则将这个元素存入PARENT_CLASS
                map.get(PARENT_CLASS).add(String.valueOf(item.get(0)));
                return;
            }
            map.get(SUB_CLASS).add(String.valueOf(item.get(1)));
        });
        return map;
    }

}
