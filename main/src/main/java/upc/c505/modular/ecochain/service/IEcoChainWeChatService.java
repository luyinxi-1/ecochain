package upc.c505.modular.ecochain.service;
import com.alibaba.fastjson.JSONObject;
import upc.c505.modular.ecochain.controller.param.EcoChainPreIdSearchParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
public interface IEcoChainWeChatService {
    String getAccessToken();

    String getPhoneNumber(String code,String appId);

    Map<String, Object> getPreId(EcoChainPreIdSearchParam searchParam);

}
