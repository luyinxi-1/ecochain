package upc.c505.modular.villageiot.util.esurfing;

import okhttp3.*;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: frd
 * @create-date: 2024/6/4 15:41
 */

public class OkHttpUtil {
    /**
     * 线程池数量：100
     */
    public static final String MAX_IDLE_CONNECTION = "100";

    /**
     * 连接超时：45000ms
     */
    public static final String CINNETC_TIMEOUT = "45000";

    /**
     * 读超时：45000ms
     */
    public static final String READ_TIMEOUT = "45000";

    /**
     * 保存长连接时间：1min
     */
    public static final String KEEP_ALIVE_DURATION = "1";

    private static OkHttpClient httpClient;

    static {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(Long.valueOf(CINNETC_TIMEOUT), TimeUnit.MILLISECONDS)
                .readTimeout(Long.valueOf(READ_TIMEOUT),TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(Integer.valueOf(MAX_IDLE_CONNECTION), Long.valueOf(KEEP_ALIVE_DURATION), TimeUnit.MINUTES)).build();
    }



    public static String sendGet(String url, String param) {
        String result = "";
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);

            Request request = new Request.Builder()
                    .url(realUrl)
                    .header("accept", "*/*")
                    .header("connection", "Keep-Alive")
                    .header("user-agent", "ehome-push")
                    .build();

            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String sendPost(String url, Map<String, Object> params) {
        String result = "";
        StringBuffer buffer  = new StringBuffer();
        try {
            // 组装入参
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
            RequestBody body = builder.build();
            // 创建request
            URL realUrl = new URL(url);
            Request request = new Request.Builder()
                    .url(realUrl)
                    .header("accept", "*/*")
                    .header("connection", "Keep-Alive")
                    .header("user-agent", "ehome-push")
                    .post(body)
                    .build();

            // 打印参数
            for (Map.Entry<String, Object> e : params.entrySet()) {
                buffer.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }

            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
