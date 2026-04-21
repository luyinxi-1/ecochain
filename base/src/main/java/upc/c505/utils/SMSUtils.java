package upc.c505.utils;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import upc.c505.sms.SendMessage;
import upc.c505.sms.SendMessageConfig;

/**
 * 腾讯云发送短信工具类
 * @author qiutian
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class SMSUtils {
    public static void sendMessage() throws TencentCloudSDKException {
        SendMessage.sendMessage(new SendMessageConfig());

    }
}
