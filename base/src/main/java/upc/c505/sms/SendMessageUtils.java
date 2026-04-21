package upc.c505.sms;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import upc.c505.exception.BusinessErrorEnum;
import upc.c505.exception.BusinessException;

import java.util.List;

/**
 * 发送信息的工具类
 *
 * @author qiutian
 */
@Slf4j
public class SendMessageUtils {
    /**
     * 常规发送信息
     *
     * @param templateId 模板id
     * @param phones     手机号
     * @param params     模板参数
     */
    public static void sendMessage(String templateId, String[] phones, String[] params) {
        try {
            SendMessage.sendMessage(
                    new SendMessageConfig()
                            .setTemplateId(templateId)
                            .setPhones(phones)
                            .setParams(params)
            );
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            throw new BusinessException(BusinessErrorEnum.MESSAGE_ERROR, e.getMessage());
        }
    }

    /**
     * 常规发送信息
     *
     * @param templateId 模板id
     * @param phones     手机号
     */
    public static void sendMessage(String templateId, String[] phones) {
        sendMessage(templateId, phones, new String[0]);
    }

    /**
     * 常规发送信息
     *
     * @param templateId 模板id
     * @param phones     手机号
     * @param params     模板参数
     */
    public static void sendMessage(String templateId, List<String> phones, String[] params) {
        sendMessage(templateId, phones.toArray(new String[0]), params);
    }

    /**
     * 常规发送信息
     *
     * @param templateId 模板id
     * @param phones     手机号
     */
    public static void sendMessage(String templateId, List<String> phones) {
        sendMessage(templateId, phones, new String[0]);
    }
}
