package upc.c505.modular.auth.service.impl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import upc.c505.modular.auth.controller.constant.WeChatConst;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

@Slf4j
@Component
public class WxService {
    /*
    用于解密微信用户的隐私信息，包括手机号等
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    public String wxDecrypt(String encryptedData, String sessionId, String vi) throws Exception {
        // 开始解密
        String json =  redisTemplate.opsForValue().get(WeChatConst.WX_SESSION_ID + sessionId);
        log.info("信息："+json);
        JSONObject jsonObject = JSON.parseObject(json);
//        拿到sessionKey
        String sessionKey = (String) jsonObject.get("session_key");
//        解密
        byte[] encData = cn.hutool.core.codec.Base64.decode(encryptedData);
        byte[] iv = cn.hutool.core.codec.Base64.decode(vi);
        byte[] key = Base64.decode(sessionKey);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//        拿到encData
        return new String(cipher.doFinal(encData), "UTF-8");
//        通过encData中的openId判断存在，存在则登录，不存在则注册后再登录
    }


}
