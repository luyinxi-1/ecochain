package upc.c505.sms;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发送信息的参数在这个类中修改，一般不需要修改这个类
 *
 * @author qiutian
 */
@Data
@Accessors(chain = true)
public class SendMessageConfig {
    public static final String SECRET_ID = "***";

    public static final String SECRET_KEY = "***";

    public static final String METHOD = "POST";

    public static final String SDK_AAP_ID = "***";

    /**
     * 模板名称
     */
    private String signName = "桥联";

    /**
     * 模板id
     */
    private String templateId = "1434268";

    /**
     * 给谁发信息
     */
    private String[] phones;

    /**
     * 模板参数，要与模板的占位符数量对应
     */
    private String[] params;
}
