package upc.c505.modular.ecochain.ecoConst;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EcoChainDistributorConst {

    public static final Map<Integer, String> USER_STATUS_MAP;
    public static final Map<Integer, String> AUTHORIZE_STATUS_MAP;

    static {
        Map<Integer, String> userStatusMap = new HashMap<>();
        userStatusMap.put(0, "正常");
        userStatusMap.put(1, "容量报警");
        userStatusMap.put(2, "授权报警");
        userStatusMap.put(3, "已超期");
        userStatusMap.put(4, "容量报警&授权预警");
        userStatusMap.put(5, "容量报警&已超期");
        USER_STATUS_MAP = Collections.unmodifiableMap(userStatusMap);

        Map<Integer, String> authorizeStatusMap = new HashMap<>();
        authorizeStatusMap.put(0, "待授权");
        authorizeStatusMap.put(1, "正常");
        authorizeStatusMap.put(2, "试用");
        authorizeStatusMap.put(3, "高级会员");
        authorizeStatusMap.put(4, "VIP会员");
        authorizeStatusMap.put(5, "高级VIP会员");
        AUTHORIZE_STATUS_MAP = Collections.unmodifiableMap(authorizeStatusMap);
    }
}
