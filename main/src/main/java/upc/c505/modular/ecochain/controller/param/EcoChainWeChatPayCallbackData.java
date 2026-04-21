package upc.c505.modular.ecochain.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
@Data
@XmlRootElement
public class EcoChainWeChatPayCallbackData {
    /**
     * 通知的唯一ID
     */
    private String id;

    /**
     * 通知创建的时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示北京时间2015年05月20日13点29分35秒。
     */
    @JsonProperty("create_time")
    private String createTime;

    /**
     * 通知的类型，支付成功通知的类型为TRANSACTION.SUCCESS
     */
    @JsonProperty("event_type")
    private String eventType;

    /**
     * 通知的资源数据类型，支付成功通知为encrypt-resource
     */
    @JsonProperty("resource_type")
    private String resourceType;

    /**
     * 通知资源数据
     */
    @JsonProperty("resource")
    private Resource resource;

    /**
     * 回调摘要
     */
    @JsonProperty("summary")
    private String summary;

    @Data
    public static class Resource {
        /**
         * 对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM
         */
        private String algorithm;

        /**
         * Base64编码后的开启/停用结果数据密文
         */
        private String ciphertext;

        /**
         * 附加数据
         */
        @JsonProperty("associated_data")
        private String associatedData;

        /**
         * 原始回调类型，为transaction
         */
        @JsonProperty("original_type")
        private String originalType;

        /**
         * 加密使用的随机串
         */
        @JsonProperty("nonce")
        private String nonce;
    }
}
