package upc.c505.modular.villageiot.util.digestutils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 摘要鉴权工具类
 * @author: frd
 * @create-date: 2024/6/24 9:59
 */

public class HttpRequestUtils {

    public static void main(String[] args) {
//        String json = HttpRequestUtils.sendPost("http://222.134.22.14:8090/LAPI/V1.0/System/Org/QueryByCondition",
//                "Limit=200&Offset=0", "admin", "123456",
//                "{\n" +
//                        "  \"Num\": \"1\",\n" +
//                        "  \"Recursion\": 2,\n" +
//                        "  \"QueryInfoList\": []\n" +
//                        "}", "json");
//        System.out.println("结果是：" + json);
//        String json = HttpRequestUtils.sendPost("222.134.22.14:8090/LAPI/V1.0/Channels/1/Media/Video/Streams/0/LiveStreamURL",
//                "?TransType=0", "admin", "123456",
//                "{}", "json");
//        System.out.println("结果是：" + json);
        String json = HttpRequestUtils.sendGet("http://222.134.22.14:8090/LAPI/V1.0/System/Security/RSA",
                "", "admin", "123456",
                "json");
        System.out.println("结果是：" + json);
    }

    static int nc = 0;    //调用次数

    /**
     * 请求头中取出的WWW-Authenticate
     */
    private static final String WWW = "WWW-Authenticate";

    /**
     * 请求头中取出的Www-Authenticate
     */
    private static final String Www = "Www-Authenticate";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";

    private static final String DELETE = "DELETE";

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url      发送请求的URL
     * @param param    请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param username 验证所需的用户名
     * @param password 验证所需的密码
     * @param type     返回xml和json格式数据，默认xml，传入json返回json数据
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String username, String password, String type) {

        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String wwwAuth = sendGet(url, param);       //发起一次授权请求
            if (wwwAuth.startsWith("WWW-Authenticate:")) {
                wwwAuth = wwwAuth.replaceFirst("WWW-Authenticate:", "");
            } else {
                return wwwAuth;
            }
            nc++;
            String urlNameString = url + (StringUtils.isNotEmpty(param) ? "?" + param : "");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            setRequestProperty(connection, wwwAuth, realUrl, username, password, GET, type);
            // 建立实际的连接
            // connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            nc = 0;
        } catch (Exception e) {
            nc = 0;
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url      发送请求的URL
     * @param param    请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param username 验证所需的用户名
     * @param password 验证所需的密码
     * @param json     请求json字符串
     * @param type     返回xml和json格式数据，默认xml，传入json返回json数据
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String username, String password, String json, String type) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String wwwAuth = sendGet(url, param);       //发起一次授权请求
            if (wwwAuth.startsWith(WWW + ":")) {
                wwwAuth = wwwAuth.replaceFirst(WWW + ":", "");
            } else {
                return wwwAuth;
            }
            nc++;
            String urlNameString = url + (StringUtils.isNotEmpty(param) ? "?" + param : "");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            // Read from the connection. Defaultis true.
            connection.setDoInput(true);
            // 默认是 GET方式
            connection.setRequestMethod(POST);
            // Post 请求不能使用缓存
            connection.setUseCaches(false);

            // 设置通用的请求属性
//            connection.setRequestProperty("Authorization","Digest username=\"admin\", realm=\"6cf17e6629ab\", nonce=\"62bb0473e1d208c1039e\", uri=\"/LAPI/V1.0/System/Org/QueryByCondition?Limit=200&Offset=0\", algorithm=\"MD5\", response=\"5d71bfcaffd1ffc11516a5cedca9507e\"");
            setRequestProperty(connection, wwwAuth, realUrl, username, password, POST, type);

            if (!StringUtils.isEmpty(json)) {
                byte[] writebytes = json.getBytes();
                connection.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = connection.getOutputStream();
                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }
            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                String errResult = formatResultInfo(connection, type);
                return errResult;
            }

            nc = 0;
        } catch (Exception e) {
            nc = 0;
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 生成授权信息
     *
     * @param authorization 上一次调用返回401的WWW-Authenticate数据
     * @param username      用户名
     * @param password      密码
     * @return 授权后的数据, 应放在http头的Authorization里
     */
    private static String getAuthorization(String authorization, String uri, String username, String password, String method) {
        uri = StringUtils.isEmpty(uri) ? "/" : uri;
        String temp = authorization.replaceFirst("Digest", "").trim().replace("MD5", "\"MD5\"").split(" ")[0];
        String json = withdrawJson(authorization);
        JSONObject jsonObject = JSON.parseObject(json);
        String nonce = jsonObject.getString("nonce");
        String realm = jsonObject.getString("realm");
        String response = Digests.http_da_calc_HA1(username, realm, password,
                nonce, method, uri, "MD5");
        //组成响应authorization
        authorization = " Digest username=\"" + username + "\"," + temp + "uri=\"" + uri + "\",algorithm=" + "\"MD5"
                + "\",response=\"" + response + "\"";
//        System.out.println(authorization);
        return authorization;
    }

    /**
     * 将返回的Authrization信息转成json
     *
     * @param authorization authorization info
     * @return 返回authrization json格式数据 如：String json = "{ \"realm\": \"Wowza\", \" domain\": \"/\", \" nonce\": \"MTU1NzgxMTU1NzQ4MDo2NzI3MWYxZTZkYjBiMjQ2ZGRjYTQ3ZjNiOTM2YjJjZA==\", \" algorithm\": \"MD5\", \" qop\": \"auth\" }";
     */
    private static String withdrawJson(String authorization) {
        String temp = authorization.replaceFirst("Digest", "").trim().replaceAll("\"", "");
        // String noncetemp = temp.substring(temp.indexOf("nonce="), temp.indexOf("uri="));
        // String json = "{\"" + temp.replaceAll("=", "\":").replaceAll(",", ",\"") + "}";
        String[] split = temp.split(",");
        Map<String, String> map = new HashMap<>();
        Arrays.asList(split).forEach(c -> {
            String c1 = c.replaceFirst("=", ":");
            String[] split1 = c1.split(":");
            map.put(split1[0].trim(), split1[1].trim());
        });
        return JSONObject.toJSONString(map);
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL                                                  所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + (StringUtils.isNotEmpty(param) ? "?" + param : "");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.connect();
            //返回401时需再次用用户名和密码请求
            //此情况返回服务器的 WWW-Authenticate 信息
            if (((HttpURLConnection) connection).getResponseCode() == 401) {
                Map<String, List<String>> map = connection.getHeaderFields();
                return ObjectUtils.allNotNull(map.get(WWW)) ? WWW + ":" + map.get(WWW).get(0) : WWW + ":" + map.get(Www).get(0);
            }

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("get请求发送失败", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * HTTP set request property
     *
     * @param connection HttpConnection
     * @param wwwAuth    授权auth
     * @param realUrl    实际url
     * @param username   验证所需的用户名
     * @param password   验证所需的密码
     * @param method     请求方式
     * @param type       返回xml和json格式数据，默认xml，传入json返回json数据
     */
    private static void setRequestProperty(HttpURLConnection connection, String wwwAuth, URL realUrl, String username, String password, String method, String type)
            throws IOException {
        if (type != null && type.equals("json")) {
            // 返回json
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "PostmanRuntime/7.29.0");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        } else {
            // 返回xml
            if (!method.equals(GET)) {
                connection.setRequestProperty("Content-Type", "application/json");
            }
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "PostmanRuntime/7.29.0");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");

        }
        //授权信息
        String authentication = getAuthorization(wwwAuth, realUrl.getPath(), username, password, method);
        connection.setRequestProperty("Authorization", authentication);
    }

    /**
     * 格式化请求返回信息，支持json和xml格式
     *
     * @param connection HttpConnection
     * @param type       指定返回数据格式，json、xml，默认xml
     * @return 返回数据
     */
    private static String formatResultInfo(HttpURLConnection connection, String type) throws IOException {
        String result = "";
        if (type != null && type.equals("json")) {
            result = String.format("{\"errCode\":%s, \"message\":%s}", connection.getResponseCode(), connection.getResponseMessage());
        } else {
            result = String.format(" <?xml version=\"1.0\" encoding=\"UTF-8\" ?> "
                    + " <wmsResponse>"
                    + " <errCode>%d</errCode>"
                    + " <message>%s</message>"
                    + " </wmsResponse>", connection.getResponseCode(), connection.getResponseMessage());
        }
        return result;
    }

}

